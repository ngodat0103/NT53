# Health Check cho HTTP
resource "google_compute_health_check" "http_health_check" {
  name               = "k8s-http-health-check"
  check_interval_sec = 5
  timeout_sec        = 5
  
  http_health_check {
    port = 30080
  }
}

# Health Check cho HTTPS
resource "google_compute_health_check" "https_health_check" {
  name               = "k8s-https-health-check"
  check_interval_sec = 5
  timeout_sec        = 5
  
  https_health_check {
    port = 30443
  }
}

# Tạo Instance Group cho worker nodes
resource "google_compute_instance_group" "k8s_workers_group" {
  name  = "k8s-workers-group"
  zone  = "us-central1-a"
  instances = google_compute_instance.k8s_workers[*].self_link
}

# Backend Service cho HTTP
resource "google_compute_backend_service" "k8s_http_backend" {
  name          = "k8s-http-backend"
  protocol      = "HTTP"
  port_name     = "http"
  health_checks = [google_compute_health_check.http_health_check.self_link]
  backend {
    group = google_compute_instance_group.k8s_workers_group.self_link
  }
}

# Backend Service cho HTTPS
resource "google_compute_backend_service" "k8s_https_backend" {
  name          = "k8s-https-backend"
  protocol      = "HTTPS"
  port_name     = "https"
  health_checks = [google_compute_health_check.https_health_check.self_link]
  backend {
    group = google_compute_instance_group.k8s_workers_group.self_link
  }
}

# Tạo Managed SSL Certificate
resource "google_compute_managed_ssl_certificate" "my_certificate" {
  name = "k8s-managed-ssl-cert"
  managed {
    domains = ["nt531.com"]
  }
}

# URL Map để định tuyến đến Backend Service cho HTTP
resource "google_compute_url_map" "k8s_http_url_map" {
  name            = "k8s-http-url-map"
  default_service = google_compute_backend_service.k8s_http_backend.self_link
}

# URL Map để định tuyến đến Backend Service cho HTTPS
resource "google_compute_url_map" "k8s_https_url_map" {
  name            = "k8s-https-url-map"
  default_service = google_compute_backend_service.k8s_https_backend.self_link
}

# Target HTTP Proxy
resource "google_compute_target_http_proxy" "k8s_http_proxy" {
  name    = "k8s-http-proxy"
  url_map = google_compute_url_map.k8s_http_url_map.self_link
}

# Target HTTPS Proxy
resource "google_compute_target_https_proxy" "k8s_https_proxy" {
  name             = "k8s-https-proxy"
  url_map          = google_compute_url_map.k8s_https_url_map.self_link
  ssl_certificates = [google_compute_managed_ssl_certificate.my_certificate.self_link]
}

# Global Forwarding Rule cho HTTP
resource "google_compute_global_forwarding_rule" "http_forwarding_rule" {
  name       = "k8s-http-forward"
  target     = google_compute_target_http_proxy.k8s_http_proxy.self_link
  port_range = "80"
  ip_protocol = "TCP"
}

# Global Forwarding Rule cho HTTPS
resource "google_compute_global_forwarding_rule" "https_forwarding_rule" {
  name       = "k8s-https-forward"
  target     = google_compute_target_https_proxy.k8s_https_proxy.self_link
  port_range = "443"
  ip_protocol = "TCP"
}
