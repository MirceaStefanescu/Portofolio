variable "name" {
  type        = string
  description = "Vault policy name."
}

variable "policy" {
  type        = string
  description = "Vault policy document in HCL format."
}
