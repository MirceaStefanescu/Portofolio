# High Error Rate

## Trigger
Alert: `HighErrorRate`

## Impact
Increased 5xx responses indicate failing requests or downstream dependencies.

## Immediate checks (5 minutes)
1. Confirm error spike on the Reliability Overview dashboard.
2. Inspect recent deploys, feature flags, or traffic shifts.
3. Look at logs for the affected pod(s).

## Diagnostics
PromQL:
- `100 * (sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / sum(rate(http_server_requests_seconds_count[5m])))`
- `sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) by (pod)`

Kubernetes:
- `kubectl logs <pod> -n <namespace> --tail=200`
- `kubectl get events -n <namespace> --sort-by=.lastTimestamp`

## Mitigation
- Roll back the last release.
- Reduce load (rate limits, scale up, or disable non-critical endpoints).
- Fix failing dependency configuration (timeouts, credentials, endpoints).

## Escalation
Page the owning service team if error rate persists > 15 minutes or impacts multiple services.

## Post-incident
- Add synthetic checks for the failing endpoint.
- Improve error budgets and alert routing.
