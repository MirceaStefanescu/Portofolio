# observability-stack module

Installs Prometheus, Grafana, and (optionally) Elasticsearch + Kibana using Helm charts.

## Usage
```
module "observability" {
  source = "../modules/observability-stack"
  namespace = "observability"
}
```
