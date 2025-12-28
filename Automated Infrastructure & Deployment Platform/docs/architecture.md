# Architecture overview

The platform is centered around a self-service portal UI and API that renders pipeline templates, generates Terraform and Helm assets, and orchestrates delivery workflows.

## Components
- Portal UI: request deployments and infra changes.
- Portal API: generates templates, exposes health and metrics endpoints.
- Template library: stored in `portal/templates` for pipelines and infra assets.
- Pipeline templates: Jenkins and GitHub Actions definitions generated per request.
- Terraform modules: reusable infra building blocks.
- Helm chart: deploys the portal and platform services.
- Vault: secret broker for pipelines and workloads.
- Prometheus and Grafana: metrics and dashboards for platform health.

## Data flow
1. Developer submits a request in the portal.
2. The portal API renders a pipeline template for Jenkins or GitHub Actions.
3. The portal API renders Terraform and Helm templates for the requested environment.
4. CI runs Terraform plan/apply to provision infra.
5. CI deploys with Helm into Kubernetes.
6. Vault injects secrets at runtime.
7. Prometheus collects metrics from `/api/metrics`, Grafana visualizes them.
8. Drift checks compare Terraform state and Kubernetes manifests to live state.
