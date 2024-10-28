# Cấu hình SSH Firewall Rule
resource "google_compute_firewall" "allow_ssh" {
  name    = "allow-ssh"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["22"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["ssh"]
}

# Cấu hình HTTP và HTTPS Firewall Rule
resource "google_compute_firewall" "allow_http_https" {
  name    = "allow-http-https"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80", "443"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["k8s"]
}
# Cấu hình Firewall Rule cho các port của worker nodes
resource "google_compute_firewall" "allow_port-worker" {
  name    = "allow-port-worker"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["30080", "30443"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["worker"]
}