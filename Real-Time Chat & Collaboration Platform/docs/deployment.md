# Deployment

## Docker Compose (Local)
1. Copy `.env.example` to `.env` in the project root.
2. Run: `docker compose -f infra/docker-compose.yml up --build`
3. Frontend: `http://localhost:4200`
4. Backend: `http://localhost:8080`

## Kubernetes via Terraform
1. Build and push images for `backend` and `frontend`.
2. Export your kubeconfig and set image variables.
3. Apply: `terraform -chdir=infra/terraform init` then `terraform -chdir=infra/terraform apply`.

## CI/CD (Jenkins)
- The pipeline builds backend and frontend, runs tests, builds Docker images, and deploys with Terraform when configured.
- Update registry credentials and cluster access in Jenkins credentials.
