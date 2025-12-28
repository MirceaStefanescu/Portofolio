require("dotenv").config();

const { createApp } = require("./app");

function startServer(port = process.env.PORT || 8080) {
  const app = createApp();
  const server = app.listen(port, () => {
    console.log(`CI/CD demo app listening on port ${port}`);
  });
  return { app, server };
}

if (require.main === module) {
  startServer();
}

module.exports = { startServer };
