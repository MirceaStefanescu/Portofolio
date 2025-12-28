const test = require("node:test");
const assert = require("node:assert/strict");
const { renderTemplate } = require("../src/templates");

test("renderTemplate replaces template keys", () => {
  const template = "app={{appName}} env={{environment}}";
  const result = renderTemplate(template, { appName: "orders", environment: "dev" });
  assert.equal(result, "app=orders env=dev");
});

test("renderTemplate drops missing values", () => {
  const template = "app={{appName}} env={{environment}}";
  const result = renderTemplate(template, { appName: "orders" });
  assert.equal(result, "app=orders env=");
});
