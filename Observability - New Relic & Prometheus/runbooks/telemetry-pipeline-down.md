# Telemetry Pipeline Down

## Trigger
Alert: `TelemetryPipelineDown`

## Impact
Metrics and traces are no longer flowing through the OpenTelemetry Collector, reducing visibility.

## Immediate checks (5 minutes)
1. Confirm the collector is down in Prometheus: `up{job="otel-collector"} == 0`
2. Check Grafana dashboards for missing data across services.

## Diagnostics
Kubernetes:
- `kubectl get pods -n observability`
- `kubectl logs <otel-collector-pod> -n observability --tail=200`

Docker Compose:
- `docker compose logs otel-collector --tail=200`

## Mitigation
- Restart the collector or roll back recent config changes.
- Verify OTLP endpoints and authentication keys (if using New Relic).

## Escalation
If telemetry loss affects multiple clusters, page the observability/platform team.

## Post-incident
- Add redundancy (multiple collectors) and health probes.
- Validate config changes with staging before production.
