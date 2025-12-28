# Local Vault example

Applies the Vault policy module against a running Vault instance.

## Run
```
export VAULT_TOKEN=dev-root-token
terraform init
terraform apply -var="vault_token=${VAULT_TOKEN}"
```
