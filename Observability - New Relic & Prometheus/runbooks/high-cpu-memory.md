# High CPU / Memory

## Trigger
Alerts: `HighCPUUsage`, `HighMemoryUsage`

## Impact
Resource saturation can lead to throttling, OOM kills, and latency spikes.

## Immediate checks (5 minutes)
1. Check CPU and memory panels on the Reliability Overview dashboard.
2. Identify top pods by usage and confirm resource limits/requests.
3. Look for noisy neighbor workloads in the same namespace.

## Diagnostics
PromQL:
- `sum(rate(container_cpu_usage_seconds_total{container!="", image!=""}[5m])) by (namespace, pod)`
- `sum(container_memory_working_set_bytes{container!="", image!=""}) by (namespace, pod)`

Kubernetes:
- `kubectl top pods -n <namespace>`
- `kubectl describe pod <pod> -n <namespace>`
- `kubectl get hpa -n <namespace>`

## Mitigation
- Scale replicas or increase resource limits/requests.
- Reduce traffic or disable expensive features.
- Tune JVM/GC or application memory settings.

## Escalation
If multiple services are saturating or node pressure is high, page the platform team.

## Post-incident
- Right-size resource requests/limits and update autoscaling policies.
- Add load tests to validate new settings.
