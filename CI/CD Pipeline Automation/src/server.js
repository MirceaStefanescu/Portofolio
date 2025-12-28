require("dotenv").config();

const { createApp } = require("./app");

function startServer(port = process.env.PORT || 8080) {
  const app = createApp();
  const server = app.listen(port, () => {
    console.log(`CI/CD demo app listening on port ${port}`);
  });

  const shutdown = (signal) => {
    console.log(`Received ${signal}. Shutting down gracefully...`);
    server.close(() => {
      process.exit(0);
    });
    setTimeout(() => process.exit(1), 10000);
  };

  process.on("SIGTERM", shutdown);
  process.on("SIGINT", shutdown);

  return { app, server };
}

if (require.main === module) {
  startServer();
}

module.exports = { startServer };
