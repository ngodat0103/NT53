provider "google" {
  credentials = file("./secrets/auth-gcp.json")
  project     = "nt531-438806"
  region      = "asia-southeast1"
  zone        = "asia-southeast1-a"
}

module "k8s-cluster" {
  source       = "./modules/instances"
  n_workers    = 3
  machine_type = "e2-medium"
  zone         = "asia-southeast1-a"
  environment  = "dev"
  region       = "asia-southeast1"
  image        = "ubuntu-2204-jammy-v20240927"
  pub_key_path = "./secrets/id-rsa.pub"
  boot_disk_size = 30
}
