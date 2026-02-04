# High Latency

## Trigger
Alert: `HighLatencyP95`

## Impact
Requests are slower than the SLO. Users may see timeouts or degraded performance.

## Immediate checks (5 minutes)
1. Validate p95 latency on the Reliability Overview dashboard.
2. Check CPU/memory saturation and error rate.
3. Inspect logs for slow or failing dependencies.

## Diagnostics
PromQL:
- `1000 * histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (le))`
- `sum(rate(http_server_requests_seconds_count[5m])) by (pod)`

Kubernetes:
- `kubectl top pods -n <namespace>`
- `kubectl describe hpa -n <namespace>`

## Mitigation
- Scale replicas and/or increase resources.
- Roll back recent changes that increased latency.
- Add caching or increase timeouts for downstream services.

## Escalation
If latency persists and affects multiple services or regions, initiate incident response.

## Post-incident
- Add latency SLOs and refine alert thresholds.
- Review slow query/endpoint traces and optimize hotspots.
