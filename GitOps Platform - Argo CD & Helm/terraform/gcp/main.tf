provider "google" {
  project = var.project_id
  region  = var.region
  zone    = var.zone
}

resource "google_container_cluster" "gke" {
  name               = var.cluster_name
  location           = var.region
  network            = "default"
  subnetwork         = "default"
  initial_node_count = var.node_count

  node_config {
    machine_type = var.machine_type
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }
}
