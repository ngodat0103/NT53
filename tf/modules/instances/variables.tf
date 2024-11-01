variable "n-worker-nodes" {
  description = "Number of instances for worker nodes"
  type        = number
  default     = 4
}
variable "n-master-nodes" {
  description = "Number of instances for master nodes"
  type        = number
  default     = 2
}
variable "machine_type" {
  description = "Machine type for instances"
  type        = string
  default     = "e2-medium"
}
variable "master-nodes-zone" {
  description = "Zone for instances"
  type        = string
  default     = "asia-southeast1-a"
}
variable "worker-nodes-zone" {
  description = "Zone for instances"
  type        = string
  default     = "asia-southeast2-a"
}
variable "environment" {
  description = "Environment for instances"
  type        = string
  default     = "dev"
}
variable "master-nodes-region" {
  description = "The region for master nodes"
  type        = string
  default     = "asia-southeast1"
}
variable "worker-nodes-region" {
  description = "The region for worker nodes"
  type        = string
  default     = "asia-southeast2"
}
variable "image" {
    description = "Image for instances"
    type        = string
    default     = "ubuntu-2204-jammy-v20240927"
}
variable "pub_key_path" {
    description = "Path to public key"
    type        = string
    default     = "./secrets/id_rsa.pub"
}
variable boot_disk_size {
  description = "Size of boot disk"
  type        = number
  default     = 30
}
variable "network" {
  description = "Network for instances"
  type        = string
  default     = "default"
}
variable "master-nodes-subnet" {
  description = "Subnetwork for instances"
  type        = string
  default     = "k8s-master-nodes-subnet"
}
variable "worker-nodes-subnet" {
  description = "Subnetwork for instances"
  type        = string
  default     = "k8s-worker-nodes-subnet"
}