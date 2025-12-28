module "platform" {
  source      = "../../modules/platform"
  environment = "dev"
  app_count   = 3
}
