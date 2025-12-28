#!/usr/bin/env bash
set -euo pipefail

app_name="${1:-platform-portal}"
namespace="${2:-default}"

echo "Rolling back Kubernetes deployment: ${app_name} (ns: ${namespace})"

if command -v kubectl >/dev/null 2>&1; then
  kubectl rollout undo "deployment/${app_name}" -n "${namespace}"
else
  echo "kubectl not found; skipping Kubernetes rollback"
fi

echo "For Terraform rollback, select the previous state snapshot and apply with a targeted plan."
