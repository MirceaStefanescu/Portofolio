const fs = require("fs");
const path = require("path");
const { createLogger, format, transports } = require("winston");

const logDir = process.env.LOG_DIR || "/var/log/app";
if (!fs.existsSync(logDir)) {
  fs.mkdirSync(logDir, { recursive: true });
}

const logger = createLogger({
  level: process.env.LOG_LEVEL || "info",
  format: format.combine(format.timestamp(), format.json()),
  transports: [
    new transports.Console(),
    new transports.File({ filename: path.join(logDir, "app.log") })
  ]
});

module.exports = logger;
