name: {{appName}}-deploy

on:
  workflow_dispatch:
  push:
    branches: ["main"]

env:
  APP_NAME: {{appName}}
  ENVIRONMENT: {{environment}}
  VAULT_ADDR: {{vaultAddr}}

jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Lint
        run: make lint
      - name: Test
        run: make test

  deploy:
    runs-on: ubuntu-latest
    needs: build-test
    steps:
      - uses: actions/checkout@v4
      - name: Terraform Plan
        run: terraform -chdir=terraform/environments/{{environment}} init && terraform -chdir=terraform/environments/{{environment}} plan
      - name: Helm Deploy
        run: helm upgrade --install {{appName}} helm/charts/platform-portal --set image.repository={{imageRepo}} --set image.tag={{imageTag}}
