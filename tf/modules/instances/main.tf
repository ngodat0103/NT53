# Tạo các địa chỉ IP tĩnh cho các node
resource "google_compute_address" "k8s_master_ip" {
  name    = "${var.environment}-master-ip"
  region  = "${var.region}"
  address_type = "INTERNAL"
}
resource "google_compute_address" "k8s_worker_ip" {
  count   = 3
  name    = "k8s-worker-${count.index}-ip"
  region  = "us-central1"
  address_type = "INTERNAL"
}

# Tạo instance cho master node
resource "google_compute_instance" "k8s_master" {
  name         = "k8s-master"
  machine_type = "e2-medium"
  zone         = "us-central1-a"

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network       = "default"
    subnetwork    = "default"
    network_ip    = google_compute_address.k8s_master_ip.address  
    access_config {}  
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
    #!/bin/bash
    # Tạo user k8s-admin
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:admin" | chpasswd
    usermod -aG sudo k8s-admin
    echo "k8s-admin ALL=(ALL) ALL" >> /etc/sudoers.d/k8s-admin
    chmod 0440 /etc/sudoers.d/k8s-admin

    # Cài đặt Python và thư viện Kubernetes
    sudo apt update
    sudo apt install -y python3
    sudo apt install -y python3-pip
    pip3 -m pip install kubernetes
  EOT

  tags = ["k8s", "ssh"]
}

# Tạo instance cho worker nodes
resource "google_compute_instance" "k8s_workers" {
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
    network       = "default"
    subnetwork    = "default"
    network_ip    = google_compute_address.k8s_worker_ip[count.index].address 
    access_config {}
  }

  metadata = {
    ssh-keys = "k8s-admin:${file("${var.pub_key_path}")}" 
  }

  metadata_startup_script = <<-EOT
    #!/bin/bash
    # Tạo user k8s-admin
    useradd -m -s /bin/bash k8s-admin
    echo "k8s-admin:admin" | chpasswd
    usermod -aG sudo k8s-admin
    echo "k8s-admin ALL=(ALL) ALL" >> /etc/sudoers.d/k8s-admin
    chmod 0440 /etc/sudoers.d/k8s-admin
    pip3 -m pip install kubernetes
  EOT

  tags = ["k8s", "ssh", "worker"]
}
