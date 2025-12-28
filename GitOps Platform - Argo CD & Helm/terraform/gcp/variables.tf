variable "project_id" {
  type        = string
  description = "GCP project ID"
}

variable "region" {
  type        = string
  description = "GCP region"
  default     = "us-central1"
}

variable "zone" {
  type        = string
  description = "GCP zone"
  default     = "us-central1-a"
}

variable "cluster_name" {
  type        = string
  description = "GKE cluster name"
  default     = "gitops-gke"
}

variable "node_count" {
  type        = number
  description = "Node count"
  default     = 2
}

variable "machine_type" {
  type        = string
  description = "Node machine type"
  default     = "e2-standard-2"
}
