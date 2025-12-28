# Deployment

## Docker Compose (Local)
1. Copy `.env.example` to `.env` in the project root.
2. Run: `docker compose -f infra/docker-compose.yml up --build`
3. Frontend: `http://localhost:4201`
4. Backend: `http://localhost:8081`

## Environment Variables
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` for PostgreSQL.
- `KAFKA_BOOTSTRAP_SERVERS` for Kafka connectivity.
- `KAFKA_CONSUMER_GROUP` (optional) to override the default per-instance consumer group.
- `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET` for OAuth2 login.
- `FRONTEND_BASE_URL` and `CORS_ALLOWED_ORIGINS` for redirects and CORS.

`.env` is local-only and should not be committed; use `.env.example` as the template.
OAuth credentials are required for the UI to access protected APIs.
For Docker Compose, set the GitHub OAuth callback to `http://localhost:4201/login/oauth2/code/github`.

## Kubernetes via Terraform
1. Build and push images for `backend` and `frontend`.
2. Export your kubeconfig and set image variables.
3. Apply: `terraform -chdir=infra/terraform init` then `terraform -chdir=infra/terraform apply`.

## CI/CD (Jenkins)
- The pipeline builds backend and frontend, runs tests, builds Docker images, and deploys with Terraform when configured.
- Update registry credentials and cluster access in Jenkins credentials.
