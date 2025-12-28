variable "app_name" {
  type    = string
  default = "cicd-demo"
}

variable "namespace" {
  type    = string
  default = "cicd-demo"
}

variable "image" {
  type    = string
  default = "cicd-demo:local"
}

variable "replicas" {
  type    = number
  default = 2
}

variable "deploy_mode" {
  type    = string
  default = "blue-green"
}

variable "release_color" {
  type    = string
  default = "blue"
}

variable "environment" {
  type    = string
  default = "dev"
}

variable "build_id" {
  type    = string
  default = "local"
}

variable "git_sha" {
  type    = string
  default = "dev"
}

variable "container_port" {
  type    = number
  default = 8080
}

variable "service_port" {
  type    = number
  default = 80
}
