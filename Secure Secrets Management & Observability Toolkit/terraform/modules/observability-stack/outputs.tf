output "namespace" {
  value       = var.namespace
  description = "Namespace where observability components are installed."
}

output "prometheus_release" {
  value       = helm_release.kube_prometheus_stack.name
  description = "Helm release name for kube-prometheus-stack."
}

output "elasticsearch_release" {
  value       = var.enable_elastic ? helm_release.elasticsearch[0].name : null
  description = "Helm release name for elasticsearch."
}

output "kibana_release" {
  value       = var.enable_elastic ? helm_release.kibana[0].name : null
  description = "Helm release name for kibana."
}
