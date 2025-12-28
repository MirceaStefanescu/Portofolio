const { getBuildInfo } = require("../../src/lib/build-info");

describe("getBuildInfo", () => {
  const originalEnv = process.env;

  beforeEach(() => {
    process.env = { ...originalEnv };
  });

  afterAll(() => {
    process.env = originalEnv;
  });

  it("uses defaults when env vars are missing", () => {
    delete process.env.ENVIRONMENT;
    delete process.env.RELEASE_COLOR;
    delete process.env.BUILD_ID;
    delete process.env.GIT_SHA;

    const info = getBuildInfo();

    expect(info.environment).toBe("dev");
    expect(info.releaseColor).toBe("blue");
    expect(info.buildId).toBe("local");
    expect(info.gitSha).toBe("dev");
    expect(info.startedAt).toMatch(/^\d{4}-\d{2}-\d{2}T/);
  });

  it("reads values from environment", () => {
    process.env.ENVIRONMENT = "staging";
    process.env.RELEASE_COLOR = "green";
    process.env.BUILD_ID = "42";
    process.env.GIT_SHA = "abc123";

    const info = getBuildInfo();

    expect(info.environment).toBe("staging");
    expect(info.releaseColor).toBe("green");
    expect(info.buildId).toBe("42");
    expect(info.gitSha).toBe("abc123");
  });
});
