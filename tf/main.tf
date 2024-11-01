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
      subnet_name   = "k8s-master-nodes-subnet"
      subnet_ip     = "172.20.0.0/16"
      subnet_region = var.master-nodes-region
      description   = "Subnet for k8s-network"
    },
    {
      subnet_name   = "k8s-worker-nodes-subnet"
      subnet_ip     = "172.21.0.0/16"
      subnet_region = var.worker-nodes-region
      description = "Subnet for worker nodes"
    }
  ]
  firewall_rules = [
    {
      name = "allow-ping-ingress"
      direction = "INGRESS"
      allow = [
        {
          protocol = "icmp"
        }
      ]
    }, 
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
  master-nodes-subnet =  module.network.subnets["${var.master-nodes-region}/k8s-master-nodes-subnet"].name
  worker-nodes-subnet = module.network.subnets["${var.worker-nodes-region}/k8s-worker-nodes-subnet"].name
  machine_type   = "e2-medium"
  environment    = "dev"
  n-master-nodes = var.n_master_nodes
  n-worker-nodes = var.n_worker_nodes
  boot_disk_size = 30
}
