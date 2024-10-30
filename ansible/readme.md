# Ansible Kubernetes Deployment

This project automates the deployment and configuration of a Kubernetes cluster using Ansible. It installs Kubernetes dependencies, sets up a single-master cluster, joins worker nodes, and deploys monitoring and application resources. The cluster includes Longhorn for persistent storage, Prometheus and Grafana for monitoring, and potentially other applications.

## Prerequisites

Before you begin, ensure you have the following software installed:

- **Ansible**: Follow the instructions for your operating system: [Ansible Installation Guide](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
- **Vagrant**: Download and install from the official website: [Vagrant](https://www.vagrantup.com/)
- **VirtualBox** (or another Vagrant provider): Download and install from the official website: [VirtualBox](https://www.virtualbox.org/)

## Setup Instructions

1. **Clone the Repository**: Clone the project repository to your local machine.

2. **Configure Inventory**: Update the inventory file (`inventory/vm.ini`) with the IP addresses and SSH keys of your target servers.

3. **Start Vagrant Machines**: Navigate to the project directory and run `vagrant up` to start the virtual machines defined in the Vagrantfile.

## Running the Project

### Prerequisites

Ensure Vagrant Machines are Running:

- Run `vagrant status` to check if the virtual machines are running. If not, start them with `vagrant up`.

### Execution Instructions

- **Deploy Kubernetes Cluster**:
  ```shell
  ansible-playbook -i inventory/vm.ini 1-setup-cluster.yaml
  ```

- **Deploy Monitoring Resources**:
  ```shell
  ansible-playbook -i inventory/vm.ini 2-k8s-monitoring-resources.yaml
  ```

- **Deploy Application Resources**:
  ```shell
  ansible-playbook -i inventory/vm.ini 3-k8s-app-resources.yaml
  ```

## Additional Information

- **Testing**: The project relies on Ansible's built-in testing mechanisms and idempotency to ensure successful deployments. Testing can be performed by running the Ansible playbooks against a test environment.

- **Deployment**: The project is deployed by running Ansible playbooks against target servers. The inventory file (`inventory/vm.ini`) defines the target servers and their roles.

- **Troubleshooting**: Ensure all prerequisites are met and the inventory file is correctly configured. Check Ansible playbook outputs for any errors during execution.
