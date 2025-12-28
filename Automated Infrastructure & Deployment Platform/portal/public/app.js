const appNameInput = document.getElementById("appName");
const environmentInput = document.getElementById("environment");
const providerSelect = document.getElementById("provider");
const appCountInput = document.getElementById("appCount");

const pipelineOutput = document.getElementById("pipelineOutput");
const pipelineStatus = document.getElementById("pipelineStatus");
const terraformOutput = document.getElementById("terraformOutput");
const helmOutput = document.getElementById("helmOutput");
const serviceStatus = document.getElementById("serviceStatus");

const generatePipelineButton = document.getElementById("generatePipeline");
const downloadPipelineButton = document.getElementById("downloadPipeline");
const generateTerraformButton = document.getElementById("generateTerraform");
const downloadTerraformButton = document.getElementById("downloadTerraform");
const generateHelmButton = document.getElementById("generateHelm");
const downloadHelmButton = document.getElementById("downloadHelm");

const latest = {
  pipeline: null,
  terraform: null,
  helm: null
};

function buildPayload() {
  return {
    appName: appNameInput.value,
    environment: environmentInput.value,
    provider: providerSelect.value,
    appCount: Number(appCountInput.value)
  };
}

async function requestTemplate(endpoint) {
  const response = await fetch(endpoint, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(buildPayload())
  });

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.error || "Request failed");
  }

  return data;
}

function updateOutput(target, value) {
  target.textContent = value;
}

function setPipelineStatus(message) {
  pipelineStatus.textContent = message;
}

function downloadFile(entry) {
  if (!entry) {
    return;
  }
  const blob = new Blob([entry.content], { type: "text/plain" });
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = entry.filename;
  document.body.appendChild(anchor);
  anchor.click();
  anchor.remove();
  URL.revokeObjectURL(url);
}

async function loadServiceStatus() {
  try {
    const response = await fetch("/api/services");
    const data = await response.json();
    renderServiceStatus(data.services || []);
  } catch (error) {
    renderServiceStatus([
      { id: "portal", name: "Portal", status: "unreachable" }
    ]);
  }
}

function renderServiceStatus(services) {
  serviceStatus.innerHTML = "";
  const portalStatus = document.createElement("li");
  portalStatus.className = "status";
  portalStatus.innerHTML =
    "<span>Portal</span><span class=\"badge healthy\">healthy</span>";
  serviceStatus.appendChild(portalStatus);

  services.forEach((service) => {
    const item = document.createElement("li");
    item.className = "status";
    const badgeClass = service.status || "unconfigured";
    item.innerHTML = `
      <span>${service.name}</span>
      <span class="badge ${badgeClass}">${badgeClass}</span>
    `;
    serviceStatus.appendChild(item);
  });
}

generatePipelineButton.addEventListener("click", async () => {
  setPipelineStatus("Generating...");
  try {
    const data = await requestTemplate("/api/pipeline");
    latest.pipeline = data;
    updateOutput(pipelineOutput, data.content);
    setPipelineStatus(`Generated ${data.filename}`);
  } catch (error) {
    updateOutput(pipelineOutput, error.message);
    setPipelineStatus("Error");
  }
});

downloadPipelineButton.addEventListener("click", () => {
  downloadFile(latest.pipeline);
});

generateTerraformButton.addEventListener("click", async () => {
  try {
    const data = await requestTemplate("/api/terraform");
    latest.terraform = data;
    updateOutput(terraformOutput, data.content);
  } catch (error) {
    updateOutput(terraformOutput, error.message);
  }
});

downloadTerraformButton.addEventListener("click", () => {
  downloadFile(latest.terraform);
});

generateHelmButton.addEventListener("click", async () => {
  try {
    const data = await requestTemplate("/api/helm");
    latest.helm = data;
    updateOutput(helmOutput, data.content);
  } catch (error) {
    updateOutput(helmOutput, error.message);
  }
});

downloadHelmButton.addEventListener("click", () => {
  downloadFile(latest.helm);
});

loadServiceStatus();
setInterval(loadServiceStatus, 10000);
