variable "environment" {
  type        = string
  description = "Target environment name."
}

variable "app_count" {
  type        = number
  description = "Number of applications managed by the platform."
  default     = 0
}
