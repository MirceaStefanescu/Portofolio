.PHONY: dev newrelic down test

dev:
	docker compose up --build

newrelic:
	OTEL_COLLECTOR_CONFIG=config-newrelic.yaml docker compose up --build

down:
	docker compose down -v

test:
	docker compose config > /dev/null
	docker run --rm -v "$(PWD)/otel-collector:/etc/otel-collector:ro" otel/opentelemetry-collector-contrib:0.96.0 validate --config=file:/etc/otel-collector/config.yaml
	docker run --rm -v "$(PWD)/otel-collector:/etc/otel-collector:ro" otel/opentelemetry-collector-contrib:0.96.0 validate --config=file:/etc/otel-collector/config-newrelic.yaml
	docker run --rm -v "$(PWD)/prometheus:/etc/prometheus:ro" prom/prometheus:v2.49.1 promtool check config /etc/prometheus/prometheus.yml
