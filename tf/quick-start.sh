#!/bin/bash

# Function to install gcloud CLI
install_gcloud() {
    echo "Installing gcloud CLI..."
    # Install gcloud for Debian/Ubuntu; customize as needed for other OSes
    curl -sSL https://sdk.cloud.google.com | bash
    exec -l $SHELL
    echo "gcloud installed successfully."
}

# Check if gcloud CLI is installed
if ! command -v gcloud &> /dev/null; then
    echo "gcloud CLI is not installed."
    read -p "Would you like to install gcloud CLI? (y/n): " install_gcloud_answer
    if [[ $install_gcloud_answer == "y" ]]; then
        install_gcloud
    else
        echo "gcloud CLI is required. Exiting."
        exit 1
    fi
else
    echo "gcloud CLI is already installed."
fi

# Initialize gcloud and authenticate
echo "Initializing gcloud..."
gcloud init
gcloud auth application-default login --quiet

# Capture project ID from gcloud config
project_id=$(gcloud config list --format="value(core.project)")
if [ -z "$project_id" ]; then
    echo "Failed to retrieve the project ID from gcloud config. Exiting."
    exit 1
fi
echo "Using project ID from gcloud config: $project_id"

# Function to install Terraform
install_terraform() {
    echo "Installing Terraform..."
    # Install Terraform for Debian/Ubuntu; customize as needed for other OSes
    sudo apt-get update && sudo apt-get install -y gnupg software-properties-common curl
    curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
    echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
    sudo apt-get update && sudo apt-get install -y terraform
    echo "Terraform installed successfully."
}

# Check if Terraform is installed
if ! command -v terraform &> /dev/null; then
    echo "Terraform is not installed."
    read -p "Would you like to install Terraform? (y/n): " install_terraform_answer
    if [[ $install_terraform_answer == "y" ]]; then
        install_terraform
    else
        echo "Terraform is required. Exiting."
        exit 1
    fi
else
    echo "Terraform is already installed."
fi

# Generate SSH key pair in ./secrets
mkdir -p ./secrets
if [[ ! -f ./secrets/id_rsa ]]; then
    ssh-keygen -t rsa -f ./secrets/id_rsa -q -N ""
    echo "SSH key pair generated at ./secrets/id_rsa and ./secrets/id_rsa.pub."
else
    echo "SSH key pair already exists at ./secrets/id_rsa and ./secrets/id_rsa.pub."
fi

# Run Terraform apply with the project_id variable
echo "Running 'terraform apply --auto-approve' with project_id: $project_id..."
terraform init
terraform fmt
terraform validate
terraform apply --auto-approve -var="project_id=$project_id"
chmod 744 ./to-ansible-inventory.sh
./to-ansible-inventory.sh
