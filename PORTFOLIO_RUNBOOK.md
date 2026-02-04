# Portfolio Runbook

Quick local run commands for each project. For full prerequisites and configuration, see each project's README.

## Launcher
- List projects: `./portfolio-launcher.sh --list`
- Show details: `./portfolio-launcher.sh --show "Cloud-Native E-Commerce Platform"`
- Run a project: `./portfolio-launcher.sh --run "Cloud-Native E-Commerce Platform"`
- List presets: `./portfolio-launcher.sh --presets`
- Show a preset: `./portfolio-launcher.sh --preset devops`

## Projects
- Automated Infrastructure & Deployment Platform: `make dev` (portal http://localhost:8080)
- Cloud-Native E-Commerce Platform: `make dev`
- DevSecOps Pipeline with SBOM & SLSA: `make dev` (optional: `cp .env.example .env`)
- Event-Driven E-Commerce Platform: `make dev`
- Full Stack E-commerce Application with Angular and Spring Boot: `docker compose up --build` (open http://localhost:4200)
- GitOps Platform - Argo CD & Helm: `make dev` (requires kind, kubectl, helm)
- Kubernetes FinOps & Cost Optimization: `make dev` (optional: `cp .env.example .env`)
- Observability - New Relic & Prometheus: `make dev` (optional: `cp .env.example .env`)
- Predictive Analytics Pipeline on Kubernetes: `make dev`
- Real-Time Chat & Collaboration Platform: `make dev`
- Secure RAG Service & Knowledge API: `make dev` (optional: `make demo`)
- Secure Secrets Management & Observability Toolkit: `make dev`
- CI/CD Pipeline Automation (path: `CI/CD Pipeline Automation`): `make dev` (optional: `cp .env.example .env`)
- LLMOps Evaluation Harness: `make dev`
