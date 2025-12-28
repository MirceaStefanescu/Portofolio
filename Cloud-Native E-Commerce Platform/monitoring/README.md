# Observability

## Prometheus + Grafana
- Prometheus scrapes `/metrics` from each service.
- Grafana is pre-provisioned with a basic service health dashboard.

Run with:
```
docker compose -f docker-compose.yml -f docker-compose.observability.yml up --build
```

Grafana:
- http://localhost:3000 (admin / admin)

## Elasticsearch + Kibana
- Elasticsearch and Kibana run alongside Filebeat.
- Filebeat reads container logs from Docker and ships them to Elasticsearch.

Kibana:
- http://localhost:5601

Notes:
- Filebeat requires access to `/var/lib/docker/containers` on the host.
