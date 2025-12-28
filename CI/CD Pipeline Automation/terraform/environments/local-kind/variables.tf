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

variable "kubeconfig" {
  type    = string
  default = "~/.kube/config"
}

variable "kube_context" {
  type    = string
  default = ""
}
