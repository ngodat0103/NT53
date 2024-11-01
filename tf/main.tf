variable "project_id" {
  description = "The project ID to deploy into"
  type        = string
}


provider "google" {
  credentials = file("./secrets/auth-gcp.json")
  project     = var.project_id
  region      = "asia-southeast1"
  zone        = "asia-southeast1-a"
}

module "network" {
  source       = "terraform-google-modules/network/google"
  version      = "9.3.0"
  network_name = "k8s-network"
  project_id   = var.project_id
  subnets = [
    {
      subnet_name   = "k8s-network-subnet"
      subnet_ip     = "172.18.0.0/16"
      subnet_region = "asia-southeast1"
      description   = "Subnet for k8s-network"
    },
  ]
  firewall_rules = [
    {
      name = "allow-ssh-ingress"
      # network       = "k8s-network"
      direction = "INGRESS"
      allow = [
        {
          protocol = "tcp"
          ports    = ["22"]
        }
      ]
      source_ranges = ["0.0.0.0/0"]
    },
    {
      name = "allow-port-for-worker-nodes"
      allow = [
        {
          protocol = "tcp"
          ports    = ["30080", "30443"]
        }
      ]
      source_ranges = ["0.0.0.0/0"]
      target_tags   = ["worker-nodes"]
    }
  ]
}
module "instances" {
  source         = "./modules/instances"
  network        = module.network.network_name
  subnetwork     = module.network.subnets_names[0]
  n_workers      = 3
  machine_type   = "e2-medium"
  region         = "asia-southeast1"
  zone           = "asia-southeast1-a"
  environment    = "dev"
  boot_disk_size = 30
}
