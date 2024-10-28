# Output địa chỉ IP public của load balancer cho HTTP
output "load_balancer_http_ip" {
  description = "The external IP address of the HTTP load balancer"
  value       = google_compute_global_forwarding_rule.http_forwarding_rule.ip_address
}

# Output địa chỉ IP public của load balancer cho HTTPS
output "load_balancer_https_ip" {
  description = "The external IP address of the HTTPS load balancer"
  value       = google_compute_global_forwarding_rule.https_forwarding_rule.ip_address
}

# Output cho IP công khai của các worker nodes
output "worker_nodes_public_ips" {
  value = google_compute_instance.k8s_workers[*].network_interface[0].access_config[0].nat_ip
  description = "Public IP addresses of the worker nodes"
}
