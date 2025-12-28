#!/usr/bin/env bash
set -euo pipefail

ENVIRONMENT="${1:-}"
IMAGE_TAG="${2:-}"
STRATEGY="${3:-}"

if [[ -z "$ENVIRONMENT" || -z "$IMAGE_TAG" ]]; then
  echo "Usage: promote.sh <environment> <image_tag> [strategy]"
  exit 1
fi

VALUES_FILE="charts/platform-service/env/${ENVIRONMENT}.yaml"
if [[ ! -f "$VALUES_FILE" ]]; then
  echo "Unknown environment: $ENVIRONMENT"
  exit 1
fi

perl -pi -e "s/^(\s*tag:).*/$1 \"${IMAGE_TAG}\"/" "$VALUES_FILE"

if [[ -n "$STRATEGY" ]]; then
  perl -pi -e "s/^(\s*strategy:).*/$1 ${STRATEGY}/" "$VALUES_FILE"
fi

printf "Updated %s with tag %s" "$VALUES_FILE" "$IMAGE_TAG"
if [[ -n "$STRATEGY" ]]; then
  printf " and strategy %s" "$STRATEGY"
fi
printf "\n"
