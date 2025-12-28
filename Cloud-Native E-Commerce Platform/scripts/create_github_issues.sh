#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${GITHUB_TOKEN:-}" ]]; then
  echo "GITHUB_TOKEN is required." >&2
  exit 1
fi

repo="${GITHUB_REPO:-}"

if [[ -z "$repo" ]]; then
  remote_url=$(git config --get remote.origin.url || true)
  if [[ "$remote_url" =~ github.com[:/](.+)\.git$ ]]; then
    repo="${BASH_REMATCH[1]}"
  fi
fi

if [[ -z "$repo" ]]; then
  echo "GITHUB_REPO is required (format: owner/repo)." >&2
  exit 1
fi

api() {
  local method="$1"
  local path="$2"
  local data="${3:-}"
  local tmp
  local status

  tmp=$(mktemp)

  if [[ -n "$data" ]]; then
    status=$(curl -sS -o "$tmp" -w "%{http_code}" -X "$method" \
      -H "Authorization: Bearer $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github+json" \
      -H "Content-Type: application/json" \
      "https://api.github.com$path" \
      -d "$data")
  else
    status=$(curl -sS -o "$tmp" -w "%{http_code}" -X "$method" \
      -H "Authorization: Bearer $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github+json" \
      "https://api.github.com$path")
  fi

  if [[ "$status" != 2* ]]; then
    echo "GitHub API error $status for $method $path" >&2
    cat "$tmp" >&2
    rm -f "$tmp"
    exit 1
  fi

  cat "$tmp"
  rm -f "$tmp"
}

url_encode() {
  python3 - <<'PY' "$1"
import sys
import urllib.parse

print(urllib.parse.quote(sys.argv[1], safe=""))
PY
}

ensure_label() {
  local name="$1"
  local color="$2"
  local description="$3"

  local encoded_name
  encoded_name=$(url_encode "$name")

  local status
  status=$(curl -sS -o /dev/null -w "%{http_code}" \
    -H "Authorization: Bearer $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github+json" \
    "https://api.github.com/repos/$repo/labels/$encoded_name")

  if [[ "$status" == "401" || "$status" == "403" ]]; then
    echo "Authorization failed when checking labels for $repo." >&2
    exit 1
  fi

  if [[ "$status" == "404" ]]; then
    local payload
    payload=$(python3 - <<PY
import json
print(json.dumps({"name": "$name", "color": "$color", "description": "$description"}))
PY
)
    api POST "/repos/$repo/labels" "$payload" >/dev/null
  fi
}

create_issue() {
  local title="$1"
  local body="$2"
  local labels_csv="$3"

  local payload
  payload=$(python3 - <<PY
import json
labels = [l.strip() for l in """$labels_csv""".split(",") if l.strip()]
print(json.dumps({"title": "$title", "body": "$body", "labels": labels}))
PY
)

  api POST "/repos/$repo/issues" "$payload" >/dev/null
}

ensure_label "good first issue" "7057ff" "Good for newcomers"
ensure_label "help wanted" "008672" "Extra attention is needed"
ensure_label "enhancement" "a2eeef" "New feature or request"
ensure_label "documentation" "0075ca" "Improvements or additions to documentation"
ensure_label "observability" "bfdadc" "Logging, metrics, tracing"

create_issue "Add Docker Compose health checks" \
"Scope:\n- Add health checks for product, order, payment services, plus RabbitMQ and Kafka.\n- Update docker-compose.yml to use depends_on with health checks.\n\nAcceptance criteria:\n- docker compose ps shows healthy containers after startup.\n- README notes the health endpoints used by each service." \
"good first issue,help wanted,enhancement"

create_issue "Add API example collection" \
"Scope:\n- Create docs/api-examples.http (or a Postman collection) with sample product and order flows.\n- Include create product, create order, get order status.\n\nAcceptance criteria:\n- Examples work against the local stack using the documented ports.\n- README links to the new file." \
"good first issue,help wanted,documentation"

create_issue "Add request correlation IDs" \
"Scope:\n- Add a request ID middleware in each service.\n- Log the request ID and include it in responses.\n\nAcceptance criteria:\n- Logs show a stable request ID for a request lifecycle.\n- API responses include an X-Request-Id header." \
"good first issue,help wanted,observability"

echo "Created issues in $repo."
