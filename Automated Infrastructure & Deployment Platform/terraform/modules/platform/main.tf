terraform {
  required_version = ">= 1.5.0"
  required_providers {
    null = {
      source  = "hashicorp/null"
      version = ">= 3.2.1"
    }
  }
}

resource "null_resource" "platform" {
  triggers = {
    environment = var.environment
    app_count   = tostring(var.app_count)
  }
}
