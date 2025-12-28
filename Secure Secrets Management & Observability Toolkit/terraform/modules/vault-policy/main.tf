terraform {
  required_providers {
    vault = {
      source  = "hashicorp/vault"
      version = ">= 3.0"
    }
  }
}

resource "vault_policy" "this" {
  name   = var.name
  policy = var.policy
}
