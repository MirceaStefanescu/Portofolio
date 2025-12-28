# Terraform (AWS)

This Terraform scaffold provisions the VPC and EKS cluster for the platform. Kafka and RabbitMQ are installed on the cluster using Helm.

## Usage
```
terraform init
terraform plan
terraform apply
```

## Post-provisioning
Configure kubeconfig:
```
aws eks update-kubeconfig --name <cluster_name> --region <region>
```

Install Kafka and RabbitMQ:
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install kafka bitnami/kafka --set replicaCount=3 --set kraft.enabled=true
helm install rabbitmq bitnami/rabbitmq
```

## Notes
- Update instance types and node group sizing per environment.
- Add RDS/Aurora for production databases.
