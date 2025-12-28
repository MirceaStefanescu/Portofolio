variable "vault_addr" {
  type        = string
  description = "Vault address."
  default     = "http://localhost:8200"
}

variable "vault_token" {
  type        = string
  description = "Vault root or admin token."
  sensitive   = true
}
