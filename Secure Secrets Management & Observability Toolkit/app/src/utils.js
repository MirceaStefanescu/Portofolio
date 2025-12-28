function maskSecret(value) {
  if (value === undefined || value === null) {
    return "";
  }

  const str = String(value);
  if (str.length <= 4) {
    return "*".repeat(str.length);
  }

  return "*".repeat(str.length - 4) + str.slice(-4);
}

module.exports = {
  maskSecret
};
