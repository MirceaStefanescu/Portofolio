# Kubernetes Deployment

## Prereqs
- A Kubernetes cluster and `kubectl` configured.
- KEDA installed for Kafka-lag autoscaling.

## Install dependencies (example)
KEDA:
```
helm repo add kedacore https://kedacore.github.io/charts
helm repo update
helm install keda kedacore/keda -n keda --create-namespace
```

Without Helm:
```
kubectl apply --server-side -f https://github.com/kedacore/keda/releases/download/v2.13.0/keda-2.13.0.yaml
```

Kafka/PostgreSQL/Elasticsearch are provided as dev-friendly manifests in `k8s/`.

## Build images
Docker Desktop / local cluster (uses local Docker images):
```
docker build -t predictive-analytics-api:latest ../api

docker build -t predictive-analytics-flink-job:latest ../flink
```

Remote cluster: push to your registry and update the image references in:
- `k8s/api-deployment.yaml`
- `k8s/flink-job.yaml`

## Deploy
```
kubectl apply -f k8s/namespace.yaml
kubectl apply -n predictive-analytics -f k8s/configmap.yaml
kubectl apply -n predictive-analytics -f k8s/secret.yaml
kubectl apply -n predictive-analytics -f k8s/zookeeper.yaml
kubectl apply -n predictive-analytics -f k8s/kafka.yaml
kubectl apply -n predictive-analytics -f k8s/kafka-init-job.yaml
kubectl apply -n predictive-analytics -f k8s/postgres-init-configmap.yaml
kubectl apply -n predictive-analytics -f k8s/postgres.yaml
kubectl apply -n predictive-analytics -f k8s/elasticsearch.yaml
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
- Elasticsearch may require `vm.max_map_count=262144` on some Linux hosts.
- The KEDA ScaledObject targets the `flink-taskmanager` deployment and uses Kafka lag on the `events` topic.
