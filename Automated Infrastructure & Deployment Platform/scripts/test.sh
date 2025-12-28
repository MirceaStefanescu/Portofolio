#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

required_paths=(
  "README.md"
  "docker-compose.yml"
  "terraform"
  "helm"
  "ci"
  ".github/workflows/ci.yml"
  "portal/public/index.html"
  "portal/public/app.js"
  "portal/public/styles.css"
  "portal/src/server.js"
  "portal/package.json"
  "portal/templates"
)

for path in "${required_paths[@]}"; do
  if [ ! -e "${root_dir}/${path}" ]; then
    echo "Missing required path: ${path}"
    exit 1
  fi
done

echo "Structure checks passed"
