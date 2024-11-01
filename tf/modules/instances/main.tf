resource "google_compute_address" "k8s-master-nodes-internal-ip" {
  count  = var.n-master-nodes
  name    = "k8s-master-${count.index}-internal-ip"
  region  = "${var.master-nodes-region}"
  address_type = "INTERNAL"
  subnetwork = var.master-nodes-subnet
}
resource "google_compute_address" "k8s-master-nodes-public-ip" {
  count   = var.n-master-nodes
  name    = "k8s-master-${count.index}-public-ip"
  region  = "${var.master-nodes-region}"
  address_type = "EXTERNAL"
}
resource "google_compute_address" "k8s-worker-nodes-internal-ip" {
  count   = var.n-worker-nodes
  subnetwork = var.worker-nodes-subnet
  name    = "k8s-worker-${count.index}-internal-ip"
  region  = var.worker-nodes-region
  address_type = "INTERNAL"
}

resource "google_compute_instance" "k8s_master" {
  count       = var.n-master-nodes
  name         = "k8s-master-${count.index}"
  machine_type = var.machine_type
  zone = var.master-nodes-zone

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network       = var.network
    subnetwork    = var.master-nodes-subnet
    network_ip    = google_compute_address.k8s-master-nodes-internal-ip[count.index].address  
    access_config {
      nat_ip = google_compute_address.k8s-master-nodes-public-ip[count.index].address
    }  
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
    #!/bin/bash
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:k8s-admin" | chpasswd
    usermod -aG sudo k8s-admin
  EOT

  tags = []
}

resource "google_compute_instance" "k8s-worker-nodes" {
  count        = var.n-worker-nodes
  name         = "k8s-worker-${count.index}"
  machine_type = var.machine_type
  zone         = var.worker-nodes-zone

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network       = var.network
    subnetwork    = var.worker-nodes-subnet
    network_ip    = google_compute_address.k8s-worker-nodes-internal-ip[count.index].address 
    access_config {}
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
   #!/bin/bash
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:k8s-admin" | chpasswd
    usermod -aG sudo k8s-admin
  EOT
  tags = []
}
