# vault-policy module

Creates a Vault policy from an HCL document.

## Usage
```
module "app_policy" {
  source = "../modules/vault-policy"
  name   = "app-policy"
  policy = file("../../vault/policies/app-policy.hcl")
}
```
