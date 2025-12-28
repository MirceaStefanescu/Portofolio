const fs = require("fs");
const path = require("path");

const templateDir = path.join(__dirname, "..", "templates");

function loadTemplate(filename) {
  const filepath = path.join(templateDir, filename);
  return fs.readFileSync(filepath, "utf8");
}

function loadTemplates() {
  return {
    jenkins: loadTemplate("Jenkinsfile.tpl"),
    github: loadTemplate("github-actions.yml.tpl"),
    terraform: loadTemplate("terraform-main.tf.tpl"),
    helm: loadTemplate("helm-values.yaml.tpl")
  };
}

function renderTemplate(template, values) {
  return template.replace(/{{\s*([a-zA-Z0-9_]+)\s*}}/g, (match, key) => {
    return Object.prototype.hasOwnProperty.call(values, key) ? values[key] : "";
  });
}

module.exports = {
  loadTemplates,
  renderTemplate
};
