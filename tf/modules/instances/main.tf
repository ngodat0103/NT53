resource "google_compute_address" "k8s_master_ip" {
  name    = "${var.environment}-master-ip"
  region  = "${var.region}"
  address_type = "INTERNAL"
  subnetwork = var.subnetwork
}
resource "google_compute_address" "k8s-worker-nodes-ip" {
  count   = var.n_workers
  subnetwork = var.subnetwork
  name    = "k8s-worker-${count.index}-ip"
  region  = var.region
  address_type = "INTERNAL"
}

resource "google_compute_instance" "k8s_master" {
  name         = "${var.environment}-k8s-master"
  machine_type = var.machine_type
  zone         = var.zone

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network       = var.network
    subnetwork    = var.subnetwork
    network_ip    = google_compute_address.k8s_master_ip.address  
    access_config {}  
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
    #!/bin/bash
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:admin" | chpasswd
    usermod -aG sudo k8s-admin
    echo "k8s-admin ALL=(ALL) ALL" >> /etc/sudoers.d/k8s-admin
    chmod 0440 /etc/sudoers.d/k8s-admin
    pip3 -m pip install kubernetes
  EOT

  tags = []
}

resource "google_compute_instance" "k8s-worker-nodes" {
  count        = var.n_workers
  name         = "${var.environment}-k8s-worker-${count.index}"
  machine_type = var.machine_type
  zone         = var.zone

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network       = var.network
    subnetwork    = var.subnetwork
    network_ip    = google_compute_address.k8s-worker-nodes-ip[count.index].address 
    access_config {}
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
    #!/bin/bash
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:admin" | chpasswd
    usermod -aG sudo k8s-admin
    echo "k8s-admin ALL=(ALL) ALL" >> /etc/sudoers.d/k8s-admin
    chmod 0440 /etc/sudoers.d/k8s-admin
    pip3 -m pip install kubernetes
  EOT
  tags = []
}
