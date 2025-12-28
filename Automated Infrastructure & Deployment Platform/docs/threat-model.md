# Threat model basics

## Assets
- Terraform state and plan outputs
- Vault tokens and dynamic secrets
- CI/CD pipeline definitions
- Kubernetes manifests and Helm values
- Portal API and template library

## Threats
- Credential leakage through logs or CI artifacts
- Unauthorized pipeline execution or config tampering
- Drift or misconfiguration in infra and cluster resources
- Unauthorized access to the portal API or template manipulation

## Mitigations
- Vault short-lived tokens and policy-scoped access
- Least-privilege CI runners and protected branches
- Drift detection with gated rollbacks and audit trails
- Input validation on portal requests and read-only templates
