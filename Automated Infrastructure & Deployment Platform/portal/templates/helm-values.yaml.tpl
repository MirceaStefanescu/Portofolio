replicaCount: 1

image:
  repository: {{imageRepo}}
  tag: {{imageTag}}
  pullPolicy: IfNotPresent

containerPort: 8080

service:
  type: ClusterIP
  port: 80

env:
  - name: APP_NAME
    value: "{{appName}}"
  - name: ENVIRONMENT
    value: "{{environment}}"
  - name: VAULT_ADDR
    value: "{{vaultAddr}}"
