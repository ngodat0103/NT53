variable "project_id" {
  description = "The project ID to deploy into"
  type        = string
  # default = "nt531-438806"
}

variable "master-nodes-region" {
    description = "The region for master nodes"
    type        = string
    default = "asia-southeast1"
}
variable "worker-nodes-region" {
    description = "The region for worker nodes"
    type        = string
    default = "asia-southeast2"
}
variable "n_master_nodes" {
  description = "Number of instances for master nodes"
  type        = number
  default     = 2
}
variable "n_worker_nodes" {
  description = "Number of instances for worker nodes"
  type        = number
  default     = 4
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