#!/usr/bin/env bash
set -euo pipefail

env_name="${1:-dev}"
tf_dir="terraform/environments/${env_name}"

if [ ! -d "${tf_dir}" ]; then
  echo "Unknown environment: ${env_name}"
  exit 1
fi

if command -v terraform >/dev/null 2>&1; then
  (
    cd "${tf_dir}"
    terraform init -backend=false
    plan_exit=0
    terraform plan -detailed-exitcode || plan_exit=$?
    if [ "${plan_exit}" -eq 2 ]; then
      echo "Terraform drift detected"
    elif [ "${plan_exit}" -ne 0 ]; then
      echo "Terraform plan failed"
      exit "${plan_exit}"
    fi
  )
else
  echo "terraform not found; skipping Terraform drift check"
fi

if ! command -v kubectl >/dev/null 2>&1; then
  echo "kubectl not found; skipping Kubernetes drift check"
  exit 0
fi

if ! kubectl cluster-info >/dev/null 2>&1; then
  echo "kubectl not configured; skipping Kubernetes drift check"
  exit 0
fi

if command -v helm >/dev/null 2>&1; then
  helm template platform-portal helm/charts/platform-portal | kubectl diff -f - || true
else
  echo "helm not found; skipping Helm diff"
fi
