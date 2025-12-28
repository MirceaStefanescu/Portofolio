# Terraform

Each cloud directory provisions a Kubernetes cluster baseline that the GitOps platform can target.

## AWS (EKS)
```
cd terraform/aws
terraform init
terraform apply
```

## Azure (AKS)
```
cd terraform/azure
terraform init
terraform apply
```

## GCP (GKE)
```
cd terraform/gcp
terraform init
terraform apply
```

Notes:
- Authenticate with your cloud provider before running `apply`.
- Update variables in `variables.tf` or use `-var` overrides as needed.
