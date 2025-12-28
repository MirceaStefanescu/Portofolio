const DEFAULTS = {
  port: 8080,
  vaultAddr: "http://vault:8200",
  logLevel: "info"
};

function parsePort(value, fallback) {
  const parsed = Number.parseInt(value, 10);
  return Number.isFinite(parsed) && parsed > 0 ? parsed : fallback;
}

function normalizeUrl(value) {
  if (!value) {
    return null;
  }
  return value.replace(/\/$/, "");
}

const config = {
  port: parsePort(process.env.PORT, DEFAULTS.port),
  vaultAddr: normalizeUrl(process.env.VAULT_ADDR) || DEFAULTS.vaultAddr,
  jenkinsUrl: normalizeUrl(process.env.JENKINS_URL),
  prometheusUrl: normalizeUrl(process.env.PROMETHEUS_URL),
  grafanaUrl: normalizeUrl(process.env.GRAFANA_URL),
  logLevel: process.env.LOG_LEVEL || DEFAULTS.logLevel
};

module.exports = {
  config,
  normalizeUrl
};
