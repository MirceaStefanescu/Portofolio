# Kubernetes Deployment

## Prereqs
- Kubernetes cluster (EKS, kind, or k3d)
- `kubectl`
- Kafka and RabbitMQ deployed in the cluster

## Install dependencies (Helm)
Kafka (Bitnami):
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install kafka bitnami/kafka --set replicaCount=1 --set kraft.enabled=true -n cloud-native-ecommerce --create-namespace
```

RabbitMQ (Bitnami):
```
helm install rabbitmq bitnami/rabbitmq --set auth.username=guest --set auth.password=guest -n cloud-native-ecommerce
```

## Deploy services
```
kubectl apply -f k8s/base/namespace.yaml
kubectl apply -f k8s/base/product-deployment.yaml
kubectl apply -f k8s/base/product-service.yaml
kubectl apply -f k8s/base/order-deployment.yaml
kubectl apply -f k8s/base/order-service.yaml
kubectl apply -f k8s/base/payment-deployment.yaml
kubectl apply -f k8s/base/payment-service.yaml
```

## Notes
- For production, replace `emptyDir` with PVCs and add network policies.
- Update image names to your registry.
