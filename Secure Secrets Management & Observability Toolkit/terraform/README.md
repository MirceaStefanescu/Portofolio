# Terraform modules

This directory contains reusable Terraform modules for Vault policies and the observability stack.

## Modules
- `modules/vault-policy`: creates Vault policies from HCL.
- `modules/observability-stack`: installs Prometheus, Grafana, Elasticsearch, and Kibana via Helm.

## Examples
- `examples/local-vault`: applies the Vault policy module against a local Vault dev instance.
