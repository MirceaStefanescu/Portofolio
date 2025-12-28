const express = require("express");
const logger = require("./logger");
const metrics = require("./metrics");
const { fetchVaultSecret } = require("./vault");
const { maskSecret } = require("./utils");

const app = express();
app.use(express.json());

const port = Number(process.env.APP_PORT || 8080);
const vaultAddr = process.env.VAULT_ADDR || "http://vault:8200";
const secretPath = process.env.VAULT_SECRET_PATH || "secret/data/app";
const roleIdFile = process.env.VAULT_ROLE_ID_FILE;
const secretIdFile = process.env.VAULT_SECRET_ID_FILE;

const refreshIntervalMs = Number(process.env.SECRET_REFRESH_INTERVAL_MS || 60000);

let cachedSecret = null;
let lastFetchMs = 0;

app.use((req, res, next) => {
  const routeLabel = req.path || "unknown";
  const endTimer = metrics.httpRequestDuration.startTimer({
    method: req.method,
    route: routeLabel
  });

  res.on("finish", () => {
    metrics.httpRequestsTotal.inc({
      method: req.method,
      route: routeLabel,
      status_code: res.statusCode
    });
    endTimer({ status_code: res.statusCode });
  });

  next();
});

async function refreshSecret() {
  try {
    const secret = await fetchVaultSecret({
      addr: vaultAddr,
      secretPath,
      roleIdFile,
      secretIdFile
    });

    cachedSecret = secret;
    lastFetchMs = Date.now();

    metrics.vaultSecretFetchTotal.inc();
    metrics.vaultSecretLastFetch.set(Math.floor(lastFetchMs / 1000));

    logger.info({
      event: "vault_secret_fetch",
      success: true,
      keys: Object.keys(secret || {})
    });
  } catch (error) {
    metrics.vaultSecretFetchFailures.inc();
    logger.error({
      event: "vault_secret_fetch",
      success: false,
      error: error.message
    });
  }
}

async function ensureSecretFresh() {
  if (!cachedSecret || Date.now() - lastFetchMs > refreshIntervalMs) {
    await refreshSecret();
  }

  return cachedSecret;
}

app.get("/", (req, res) => {
  res.json({
    service: "secure-secrets-toolkit",
    status: "ok"
  });
});

app.get("/health", async (req, res) => {
  const secret = await ensureSecretFresh();
  res.json({
    status: "ok",
    vault: secret ? "ok" : "unavailable",
    last_secret_fetch: lastFetchMs ? new Date(lastFetchMs).toISOString() : null
  });
});

app.get("/secret", async (req, res) => {
  const secret = await ensureSecretFresh();

  if (!secret) {
    return res.status(503).json({
      status: "unavailable",
      message: "Vault secret not available"
    });
  }

  return res.json({
    db_password_masked: maskSecret(secret.db_password || ""),
    rotated_at: secret.rotated_at || null,
    fetched_at: lastFetchMs ? new Date(lastFetchMs).toISOString() : null
  });
});

app.get("/metrics", async (req, res) => {
  res.set("Content-Type", metrics.register.contentType);
  res.end(await metrics.register.metrics());
});

app.listen(port, () => {
  logger.info({
    event: "app_started",
    port,
    vault_addr: vaultAddr,
    secret_path: secretPath
  });

  refreshSecret();
  if (refreshIntervalMs > 0) {
    setInterval(refreshSecret, refreshIntervalMs).unref();
  }
});
