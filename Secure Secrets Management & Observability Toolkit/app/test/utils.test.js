const test = require("node:test");
const assert = require("node:assert/strict");
const { maskSecret } = require("../src/utils");

test("maskSecret hides all but last four characters", () => {
  assert.equal(maskSecret("abcd1234"), "****1234");
});

test("maskSecret masks short secrets", () => {
  assert.equal(maskSecret("abc"), "***");
});

test("maskSecret handles empty values", () => {
  assert.equal(maskSecret(""), "");
  assert.equal(maskSecret(null), "");
});
