terraform {
  required_providers {
    helm = {
      source  = "hashicorp/helm"
      version = ">= 2.10"
    }
  }
}

resource "helm_release" "kube_prometheus_stack" {
  name             = "kube-prometheus-stack"
  repository       = "https://prometheus-community.github.io/helm-charts"
  chart            = "kube-prometheus-stack"
  namespace        = var.namespace
  create_namespace = true
  version          = var.prometheus_stack_version
}

resource "helm_release" "elasticsearch" {
  count            = var.enable_elastic ? 1 : 0
  name             = "elasticsearch"
  repository       = "https://helm.elastic.co"
  chart            = "elasticsearch"
  namespace        = var.namespace
  create_namespace = true
  version          = var.elasticsearch_version

  values = [yamlencode({
    replicas           = 1,
    minimumMasterNodes = 1,
    esJavaOpts         = "-Xms512m -Xmx512m"
  })]
}

resource "helm_release" "kibana" {
  count            = var.enable_elastic ? 1 : 0
  name             = "kibana"
  repository       = "https://helm.elastic.co"
  chart            = "kibana"
  namespace        = var.namespace
  create_namespace = true
  version          = var.kibana_version

  depends_on = [helm_release.elasticsearch]
}
