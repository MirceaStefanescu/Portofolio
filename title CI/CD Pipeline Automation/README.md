# CI/CD Pipeline Automation

Automated CI/CD pipeline for web teams that need repeatable builds,
containerized releases, and zero-downtime deployments across dev, staging, and
production environments.

## Demo
- Live: TBD
- Video or GIF: TBD
- Screenshots: TBD

## Why this exists
Teams often ship changes through inconsistent pipelines that are hard to audit
and impossible to reproduce. This project standardizes CI/CD for web
applications: Jenkins orchestrates builds and tests, Docker packages immutable
images, Terraform provisions environments, Ansible applies configuration, and
Kubernetes deploys with blue-green or rolling strategies. Prometheus and Grafana
surface health and deployment signals across every environment.

## Features
- Jenkins pipeline with build, test, container, and deploy stages.
- Docker image packaging with environment-specific tags.
- Terraform provisioning for Kubernetes namespaces and app resources.
- Ansible configuration management for environment settings and rollouts.
- Blue-green and rolling deployment options with zero downtime.
- Prometheus metrics and Grafana dashboards for monitoring and alerting.

## Architecture
```mermaid
flowchart LR
  Dev[Developer] --> Git[Git Repo]
  Git --> Jenkins[Jenkins Pipeline]
  Jenkins --> Tests[Unit / Integration / E2E Tests]
  Jenkins --> Build[Docker Build]
  Build --> Registry[Container Registry]
  Jenkins --> TF[Terraform Provisioning]
  Jenkins --> Ansible[Ansible Config]
  TF --> K8s[Kubernetes Environments]
  Ansible --> K8s
  Registry --> K8s
  K8s --> Prom[Prometheus]
  Prom --> Grafana[Grafana Alerts + Dashboards]
```

Jenkins orchestrates testing, image builds, provisioning, and deployments.
Terraform creates the environment on Kubernetes, Ansible applies configuration
and rollout options, and the deployment pulls immutable Docker images. Metrics
and alerts flow to Prometheus and Grafana to validate releases and uptime.

## Tech stack
- Backend: Node.js + Express for a lightweight demo web service.
- Containerization: Docker for immutable, reproducible images.
- Infra: Terraform + Kubernetes for environment provisioning.
- Config management: Ansible for config templates and rollout actions.
- CI/CD: Jenkins for pipeline orchestration.
- Observability: Prometheus + Grafana for metrics and alerts.
- Tests: Jest + Supertest for unit, integration, and e2e coverage.

## Quickstart (local)
Prereqs:
- Node.js 20+
- Docker and Docker Compose
- Make (optional)

Run:
```
make dev
```

Access:
- App: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin / admin)

## Tests
```
make test
```

## Security
Secrets: use `.env` (see `.env.example`). Never commit real credentials. Store
registry, Jenkins, and cloud keys in a secret manager or Jenkins credentials
store. Apply Kubernetes RBAC and network policies for production.

## Notes / limitations
- Status: fully functional local demo; cloud provisioning requires credentials.
- Jenkins pipeline assumes Docker, Terraform, kubectl, and Ansible are available.

## Roadmap / tradeoffs
- Add policy-as-code (OPA/Conftest) gates for Terraform and Kubernetes changes.
- Add canary deployments alongside blue-green.
- Tradeoff: blue-green releases double temporary capacity to reduce risk.

## Decisions and rationale
- Jenkins remains common in enterprise environments, so it anchors the pipeline.
- Terraform standardizes environment creation and keeps infra auditable.
- Ansible is used for repeatable config changes across environments.

## Tags
devops, docker, jenkins, ansible, terraform, kubernetes, ci/cd

## Skills and tools
Tools and software: jenkins, docker, terraform, ansible, kubernetes, prometheus,
grafana.

Skills: devops, ci/cd.
