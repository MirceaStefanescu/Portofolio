# Kubernetes Deployment

## Prereqs
- A Kubernetes cluster and `kubectl` configured.
- Kafka, PostgreSQL, and Elasticsearch reachable from the cluster.
- KEDA installed for Kafka-lag autoscaling.

## Install dependencies (example)
KEDA:
```
helm repo add kedacore https://kedacore.github.io/charts
helm repo update
helm install keda kedacore/keda -n keda --create-namespace
```

Kafka/PostgreSQL/Elasticsearch: use Helm charts or managed services and update `k8s/configmap.yaml` with their endpoints.

## Build and push images
```
docker build -t ghcr.io/your-user/predictive-analytics-api:latest ../api

docker build -t ghcr.io/your-user/predictive-analytics-flink-job:latest ../flink

docker push ghcr.io/your-user/predictive-analytics-api:latest

docker push ghcr.io/your-user/predictive-analytics-flink-job:latest
```

Update the image references in:
- `k8s/api-deployment.yaml`
- `k8s/flink-job.yaml`

## Deploy
```
kubectl apply -f k8s/namespace.yaml
kubectl apply -n predictive-analytics -f k8s/configmap.yaml
kubectl apply -n predictive-analytics -f k8s/secret.yaml
kubectl apply -n predictive-analytics -f k8s/flink-jobmanager.yaml
kubectl apply -n predictive-analytics -f k8s/flink-taskmanager.yaml
kubectl apply -n predictive-analytics -f k8s/flink-job.yaml
kubectl apply -n predictive-analytics -f k8s/api-deployment.yaml
kubectl apply -n predictive-analytics -f k8s/api-service.yaml
kubectl apply -n predictive-analytics -f k8s/keda-scaledobject.yaml
```

## Verify
```
kubectl -n predictive-analytics get pods
kubectl -n predictive-analytics port-forward svc/predictive-analytics-api 8082:8080
kubectl -n predictive-analytics port-forward svc/flink-jobmanager 8081:8081
```

API health:
```
curl http://localhost:8082/api/health
```

## Notes
- Update `k8s/configmap.yaml` with your Kafka, PostgreSQL, and Elasticsearch endpoints.
- The KEDA ScaledObject targets the `flink-taskmanager` deployment and uses Kafka lag on the `events` topic.
