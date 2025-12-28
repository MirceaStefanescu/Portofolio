const express = require("express");
const fs = require("fs");
const path = require("path");

const app = express();
const port = process.env.PORT || 8080;

const templateDir = path.join(__dirname, "..", "templates");
const publicDir = path.join(__dirname, "..", "public");

const templates = {
  jenkins: loadTemplate("Jenkinsfile.tpl"),
  github: loadTemplate("github-actions.yml.tpl"),
  terraform: loadTemplate("terraform-main.tf.tpl"),
  helm: loadTemplate("helm-values.yaml.tpl")
};

const metrics = {
  pipeline: 0,
  terraform: 0,
  helm: 0,
  services: 0
};

app.use(express.json({ limit: "1mb" }));
app.use(express.static(publicDir));

app.get("/api/health", (req, res) => {
  res.json({ status: "ok" });
});

app.get("/api/services", async (req, res) => {
  metrics.services += 1;

  const serviceChecks = [
    {
      id: "jenkins",
      name: "Jenkins",
      url: process.env.JENKINS_URL,
      path: "/login"
    },
    {
      id: "vault",
      name: "Vault",
      url: process.env.VAULT_ADDR,
      path: "/v1/sys/health"
    },
    {
      id: "prometheus",
      name: "Prometheus",
      url: process.env.PROMETHEUS_URL,
      path: "/-/ready"
    },
    {
      id: "grafana",
      name: "Grafana",
      url: process.env.GRAFANA_URL,
      path: "/api/health"
    }
  ];

  const results = await Promise.all(
    serviceChecks.map(async (service) => {
      if (!service.url) {
        return {
          id: service.id,
          name: service.name,
          status: "unconfigured"
        };
      }

      const url = `${service.url}${service.path}`;
      try {
        const response = await fetchWithTimeout(url, 2000);
        const status = response.ok ? "healthy" : "unhealthy";
        return {
          id: service.id,
          name: service.name,
          status,
          httpStatus: response.status
        };
      } catch (error) {
        return {
          id: service.id,
          name: service.name,
          status: "unreachable"
        };
      }
    })
  );

  res.json({ services: results });
});

app.post("/api/pipeline", (req, res) => {
  const input = normalizeInput(req.body);
  if (!input.valid) {
    res.status(400).json({ error: input.error });
    return;
  }

  const provider = input.provider;
  const template = provider === "jenkins" ? templates.jenkins : templates.github;
  metrics.pipeline += 1;

  const content = renderTemplate(template, {
    appName: input.appName,
    environment: input.environment,
    vaultAddr: process.env.VAULT_ADDR || "http://vault:8200",
    imageRepo: input.appName,
    imageTag: "latest"
  });

  res.json({
    filename: provider === "jenkins" ? "Jenkinsfile" : "ci.yml",
    content
  });
});

app.post("/api/terraform", (req, res) => {
  const input = normalizeInput(req.body);
  if (!input.valid) {
    res.status(400).json({ error: input.error });
    return;
  }

  metrics.terraform += 1;

  const content = renderTemplate(templates.terraform, {
    environment: input.environment,
    appCount: input.appCount
  });

  res.json({
    filename: `main-${input.environment}.tf`,
    content
  });
});

app.post("/api/helm", (req, res) => {
  const input = normalizeInput(req.body);
  if (!input.valid) {
    res.status(400).json({ error: input.error });
    return;
  }

  metrics.helm += 1;

  const content = renderTemplate(templates.helm, {
    appName: input.appName,
    environment: input.environment,
    imageRepo: input.appName,
    imageTag: "latest",
    vaultAddr: process.env.VAULT_ADDR || "http://vault:8200"
  });

  res.json({
    filename: `values-${input.environment}.yaml`,
    content
  });
});

app.get("/api/metrics", (req, res) => {
  res.set("Content-Type", "text/plain; version=0.0.4");
  res.send(
    [
      "# HELP portal_requests_total Total API requests.",
      "# TYPE portal_requests_total counter",
      `portal_requests_total{endpoint=\"pipeline\"} ${metrics.pipeline}`,
      `portal_requests_total{endpoint=\"terraform\"} ${metrics.terraform}`,
      `portal_requests_total{endpoint=\"helm\"} ${metrics.helm}`,
      `portal_requests_total{endpoint=\"services\"} ${metrics.services}`
    ].join("\n")
  );
});

app.listen(port, () => {
  console.log(`Portal listening on port ${port}`);
});

function loadTemplate(filename) {
  const filepath = path.join(templateDir, filename);
  return fs.readFileSync(filepath, "utf8");
}

function renderTemplate(template, values) {
  return template.replace(/{{\s*([a-zA-Z0-9_]+)\s*}}/g, (match, key) => {
    return Object.prototype.hasOwnProperty.call(values, key) ? values[key] : "";
  });
}

function normalizeInput(body) {
  const rawAppName = String(body.appName || "").trim().toLowerCase();
  const rawEnvironment = String(body.environment || "").trim().toLowerCase();
  const provider = String(body.provider || "github").trim().toLowerCase();
  const appCount = Number.isFinite(Number(body.appCount))
    ? Math.max(0, Math.min(50, Number(body.appCount)))
    : 1;

  if (!isSafeName(rawAppName)) {
    return { valid: false, error: "App name must be lowercase letters, numbers, or hyphens." };
  }

  if (!isSafeName(rawEnvironment)) {
    return {
      valid: false,
      error: "Environment must be lowercase letters, numbers, or hyphens."
    };
  }

  if (provider !== "jenkins" && provider !== "github") {
    return { valid: false, error: "Provider must be jenkins or github." };
  }

  return {
    valid: true,
    appName: rawAppName,
    environment: rawEnvironment,
    provider,
    appCount
  };
}

function isSafeName(value) {
  return Boolean(value) && /^[a-z0-9-]+$/.test(value);
}

async function fetchWithTimeout(url, timeoutMs) {
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), timeoutMs);
  try {
    return await fetch(url, { signal: controller.signal });
  } finally {
    clearTimeout(timeout);
  }
}
