#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="${NAMESPACE:-cicd-demo}"
APP_NAME="${APP_NAME:-cicd-demo}"
IMAGE="${IMAGE:-cicd-demo:local}"

kubectl apply -f k8s/base/namespace.yaml
kubectl apply -f k8s/base/configmap.yaml
kubectl apply -f k8s/rolling

kubectl -n "${NAMESPACE}" set image "deployment/${APP_NAME}-rolling" \
  app="${IMAGE}"

kubectl -n "${NAMESPACE}" rollout status "deployment/${APP_NAME}-rolling"
