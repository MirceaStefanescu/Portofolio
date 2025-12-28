const startedAt = new Date().toISOString();

function getBuildInfo() {
  return {
    service: "cicd-pipeline-demo",
    environment: process.env.ENVIRONMENT || "dev",
    releaseColor: process.env.RELEASE_COLOR || "blue",
    buildId: process.env.BUILD_ID || "local",
    gitSha: process.env.GIT_SHA || "dev",
    startedAt
  };
}

module.exports = { getBuildInfo };
