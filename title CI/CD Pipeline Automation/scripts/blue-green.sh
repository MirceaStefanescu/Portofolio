#!/usr/bin/env bash
set -euo pipefail

COLOR="${1:-blue}"
NAMESPACE="${NAMESPACE:-cicd-demo}"
APP_NAME="${APP_NAME:-cicd-demo}"

kubectl apply -f k8s/base/namespace.yaml
kubectl apply -f k8s/base/configmap.yaml
kubectl apply -f k8s/blue-green

kubectl -n "${NAMESPACE}" patch service "${APP_NAME}" \
  -p "{\"spec\":{\"selector\":{\"app\":\"${APP_NAME}\",\"color\":\"${COLOR}\"}}}"

kubectl -n "${NAMESPACE}" rollout status "deployment/${APP_NAME}-${COLOR}"
