# Module Docs

This folder contains supporting documentation for the e-commerce module.

## Architecture

- `docs/architecture.md`

## Kubernetes

Manifests live under `docs/k8s/`:

- `backend-deployment.yaml`
- `frontend-deployment.yaml`
- `mysql-statefulset.yaml`
- `ingress.yaml`

Remember to create the `ecommerce-secrets` secret before deploying. At minimum,
include:

- `mysql-username`
- `mysql-password`
- `mysql-root-password`
- `jwt-secret`
- `stripe-secret` (optional for demo mode)

## Screenshots

Add UI screenshots under `docs/screenshots/`.
