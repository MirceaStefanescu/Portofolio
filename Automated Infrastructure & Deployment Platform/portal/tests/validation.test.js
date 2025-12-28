const test = require("node:test");
const assert = require("node:assert/strict");
const { normalizeInput, isSafeName } = require("../src/validation");

test("isSafeName accepts lowercase names with hyphens", () => {
  assert.equal(isSafeName("orders-api"), true);
  assert.equal(isSafeName("Orders"), false);
  assert.equal(isSafeName("orders_api"), false);
});

test("normalizeInput defaults provider to github", () => {
  const result = normalizeInput({ appName: "orders", environment: "dev" });
  assert.equal(result.valid, true);
  assert.equal(result.provider, "github");
});

test("normalizeInput clamps appCount to range", () => {
  const result = normalizeInput({
    appName: "orders",
    environment: "dev",
    appCount: 999
  });
  assert.equal(result.valid, true);
  assert.equal(result.appCount, 50);
});
