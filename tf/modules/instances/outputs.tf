output "master-nodes-public-ip" {
  description = "Public IP of master nodes"
  value       = google_compute_instance.k8s_master[*].network_interface[*].access_config[*].nat_ip
}
output "worker-nodes-public-ip" {
  description = "Public IP of worker nodes"
  value       = google_compute_instance.k8s-worker-nodes[*].network_interface[*].access_config[*].nat_ip
}