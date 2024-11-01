variable "n_workers" {
  description = "Number of instances for worker nodes"
  type        = number
  default     = 3
}
variable "machine_type" {
  description = "Machine type for instances"
  type        = string
  default     = "e2-medium"
}
variable "zone" {
  description = "Zone for instances"
  type        = string
  default     = "asia-southeast1-a"
}
variable "environment" {
  description = "Environment for instances"
  type        = string
  default     = "dev"
}
variable "region" {
  description = "Region for instances"
  type        = string
  default     = "asia-southeast1"
}
variable "image" {
    description = "Image for instances"
    type        = string
    default     = "ubuntu-2204-jammy-v20240927"
}
variable "pub_key_path" {
    description = "Path to public key"
    type        = string
    default     = "./secrets/id-rsa.pub"
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
variable "subnetwork" {
  description = "Subnetwork for instances"
  type        = string
  default     = "default"
}