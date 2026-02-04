# Service Down

## Trigger
Alert: `ServiceDown`

## Impact
Traffic is failing to reach one or more targets. Customer requests may be timing out or failing.

## Immediate checks (5 minutes)
1. Confirm scope in Grafana: request rate, error rate, and logs for the affected service.
2. Check target health in Prometheus: `up{job=~".+"} == 0`
3. Review recent deploys/rollouts and config changes.

## Diagnostics
PromQL:
- `up{job=~".+"}`
- `sum(rate(http_server_requests_seconds_count[5m])) by (job)`

Kubernetes:
- `kubectl get pods -n <namespace>`
- `kubectl describe pod <pod> -n <namespace>`
- `kubectl logs <pod> -n <namespace> --tail=200`

## Mitigation
- Roll back the last release.
- Scale replicas or restart stuck pods.
- Disable failing feature flags or config.

## Escalation
If multiple services are down or cluster health is degraded, page the platform team and initiate incident response.

## Post-incident
- Add missing readiness/liveness probes.
- Improve rollback automation and canary coverage.
- Update alert thresholds and runbook notes based on findings.
