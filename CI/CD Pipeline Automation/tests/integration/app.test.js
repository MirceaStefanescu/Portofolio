const request = require("supertest");
const { createApp } = require("../../src/app");

describe("app integration", () => {
  it("returns health status", async () => {
    const app = createApp();
    const response = await request(app).get("/health");

    expect(response.status).toBe(200);
    expect(response.body.status).toBe("ok");
    expect(response.body.build).toBeDefined();
  });

  it("returns pipeline info", async () => {
    const app = createApp();
    const response = await request(app).get("/api/info");

    expect(response.status).toBe(200);
    expect(response.body.pipeline.stages).toContain("docker-build");
  });
});
