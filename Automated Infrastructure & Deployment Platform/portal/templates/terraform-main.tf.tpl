module "platform" {
  source      = "../../modules/platform"
  environment = "{{environment}}"
  app_count   = {{appCount}}
}
