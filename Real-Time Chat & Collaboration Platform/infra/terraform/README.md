# Terraform Deployment

This Terraform configuration deploys the full stack (PostgreSQL, Kafka, backend, frontend) into an existing Kubernetes cluster.

## Prerequisites
- Kubernetes cluster and kubeconfig
- Docker images for backend and frontend built and pushed to a registry

## Example
```bash
export KUBECONFIG=~/.kube/config
terraform -chdir=infra/terraform init
terraform -chdir=infra/terraform apply \
  -var "backend_image=your-registry/realtime-chat-backend:latest" \
  -var "frontend_image=your-registry/realtime-chat-frontend:latest" \
  -var "frontend_base_url=http://your-frontend-domain" \
  -var "cors_allowed_origins=http://your-frontend-domain" \
  -var "github_client_id=..." \
  -var "github_client_secret=..."
```

## Notes
- Kafka and PostgreSQL are deployed as single-instance workloads for demo use.
- For production, replace them with managed services or dedicated operators.
