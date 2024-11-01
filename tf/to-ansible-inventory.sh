#!/bin/bash

# JSON file containing the IP information
json_file="terraform-output.json"

# Extract master nodes IPs and format them
touch instances.ini
echo "[master-nodes]" > instances.ini
jq -r '.["master-nodes-public-ip"].value[][][]' "$json_file" >>instances.ini

# Extract worker nodes IPs and format them
echo "[worker-nodes]" >> instances.ini
jq -r '.["worker-nodes-public-ip"].value[][][]' "$json_file" >>instances.ini
