variable "namespace" {
  type        = string
  default     = "observability"
  description = "Kubernetes namespace for the observability stack."
}

variable "prometheus_stack_version" {
  type        = string
  default     = null
  description = "Helm chart version for kube-prometheus-stack."
}

variable "elasticsearch_version" {
  type        = string
  default     = null
  description = "Helm chart version for elasticsearch."
}

variable "kibana_version" {
  type        = string
  default     = null
  description = "Helm chart version for kibana."
}

variable "enable_elastic" {
  type        = bool
  default     = true
  description = "Whether to deploy Elasticsearch and Kibana."
}
