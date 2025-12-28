async function fetchJson(path) {
  const response = await fetch(path);
  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }
  return response.json();
}

async function loadStatus() {
  const [info, status] = await Promise.all([
    fetchJson("/api/info"),
    fetchJson("/api/status")
  ]);

  document.getElementById("env").textContent = status.environment;
  document.getElementById("color").textContent = status.releaseColor;
  document.getElementById("build").textContent = status.buildId;

  document.getElementById("service").textContent = info.build.service;
  document.getElementById("sha").textContent = info.build.gitSha;
  document.getElementById("started").textContent = info.build.startedAt;

  const stages = document.getElementById("stages");
  stages.innerHTML = "";
  info.pipeline.stages.forEach((stage) => {
    const item = document.createElement("li");
    item.textContent = stage;
    stages.appendChild(item);
  });
}

loadStatus().catch((error) => {
  console.error(error);
  document.getElementById("env").textContent = "unavailable";
  document.getElementById("color").textContent = "unavailable";
  document.getElementById("build").textContent = "unavailable";
});
