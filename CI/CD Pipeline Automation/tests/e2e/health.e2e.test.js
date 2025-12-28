const { startServer } = require("../../src/server");

describe("e2e", () => {
  let server;
  let baseUrl;

  beforeAll(() => {
    const started = startServer(0);
    server = started.server;
    const { port } = server.address();
    baseUrl = `http://127.0.0.1:${port}`;
  });

  afterAll((done) => {
    server.close(done);
  });

  it("serves the homepage", async () => {
    const response = await fetch(`${baseUrl}/`);

    expect(response.status).toBe(200);
    const body = await response.text();
    expect(body).toContain("CI/CD Pipeline Automation");
  });

  it("exposes metrics", async () => {
    const response = await fetch(`${baseUrl}/metrics`);

    expect(response.status).toBe(200);
    const body = await response.text();
    expect(body).toContain("http_requests_total");
  });
});
