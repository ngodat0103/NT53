#!/bin/bash
project_id=$(gcloud config list --format="value(core.project)")
if [ -z "$project_id" ]; then
    echo "Failed to retrieve the project ID from gcloud config. Exiting."
    exit 1
fi
alias tfplan="terraform plan -var project_id=$project_id"
alias tfapply="terraform apply --auto-approve -var project_id=$project_id"
alias tfdestroy="terraform destroy --auto-approve -var project_id=$project_id"
