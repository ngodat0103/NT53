# Kubernetes Setup Diagram
```mermaid
---
title: Ansible Playbook Grapher
---
%%{ init: { "flowchart": { "curve": "bumpX" } } }%%
flowchart LR
	%% Start of the playbook 'setup-k8s.yaml'
	playbook_70a5566f("setup-k8s.yaml")
		%% Start of the play 'Play: Set up Kubernetes cluster (0)'
		play_3b07883a["Play: Set up Kubernetes cluster (0)"]
		style play_3b07883a fill:#796b53,color:#ffffff
		playbook_70a5566f --> |"1"| play_3b07883a
		linkStyle 0 stroke:#796b53,color:#796b53
			task_9f53edaf["[task]  Disable swab"]
			style task_9f53edaf stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"1"| task_9f53edaf
			linkStyle 1 stroke:#796b53,color:#796b53
			task_eac2cbea["[task]  Remove swap entry from /etc/fstab"]
			style task_eac2cbea stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"2"| task_eac2cbea
			linkStyle 2 stroke:#796b53,color:#796b53
			task_c85db5a0["[task]  Install required packages"]
			style task_c85db5a0 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"3"| task_c85db5a0
			linkStyle 3 stroke:#796b53,color:#796b53
			task_8e28c459["[task]  Add Kubernetes GPG key"]
			style task_8e28c459 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"4"| task_8e28c459
			linkStyle 4 stroke:#796b53,color:#796b53
			task_392f2b2c["[task]  Add Kubernetes repository"]
			style task_392f2b2c stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"5"| task_392f2b2c
			linkStyle 5 stroke:#796b53,color:#796b53
			task_82077dbb["[task]  Install Kubernetes components"]
			style task_82077dbb stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"6"| task_82077dbb
			linkStyle 6 stroke:#796b53,color:#796b53
			task_b72ba596["[task]  Start kubelet service"]
			style task_b72ba596 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"7"| task_b72ba596
			linkStyle 7 stroke:#796b53,color:#796b53
			task_b9199391["[task]  Install containerd"]
			style task_b9199391 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"8"| task_b9199391
			linkStyle 8 stroke:#796b53,color:#796b53
			task_58dc50ba["[task]  Create containerd configuration directory"]
			style task_58dc50ba stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"9"| task_58dc50ba
			linkStyle 9 stroke:#796b53,color:#796b53
			task_57ab15b3["[task]  Genererate defaultl containerd configuration"]
			style task_57ab15b3 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"10"| task_57ab15b3
			linkStyle 10 stroke:#796b53,color:#796b53
			task_05f89b6c["[task]  Configure containerd to use systemd as cgroup driver"]
			style task_05f89b6c stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"11"| task_05f89b6c
			linkStyle 11 stroke:#796b53,color:#796b53
			task_e1808961["[task]  Restart containerd service"]
			style task_e1808961 stroke:#796b53,fill:#ffffff
			play_3b07883a --> |"12"| task_e1808961
			linkStyle 12 stroke:#796b53,color:#796b53
		%% End of the play 'Play: Set up Kubernetes cluster (0)'
		%% Start of the play 'Play: Initialize Kubernetes master node (0)'
		play_f989ecef["Play: Initialize Kubernetes master node (0)"]
		style play_f989ecef fill:#0144cb,color:#ffffff
		playbook_70a5566f --> |"2"| play_f989ecef
		linkStyle 13 stroke:#0144cb,color:#0144cb
			task_c1bb0d49["[task]  Initialize Kubernetes master node"]
			style task_c1bb0d49 stroke:#0144cb,fill:#ffffff
			play_f989ecef --> |"1"| task_c1bb0d49
			linkStyle 14 stroke:#0144cb,color:#0144cb
			task_fa1ade92["[task]  Create .kube directory"]
			style task_fa1ade92 stroke:#0144cb,fill:#ffffff
			play_f989ecef --> |"2"| task_fa1ade92
			linkStyle 15 stroke:#0144cb,color:#0144cb
			task_5199e040["[task]  Copy Kubernetes configuration file"]
			style task_5199e040 stroke:#0144cb,fill:#ffffff
			play_f989ecef --> |"3"| task_5199e040
			linkStyle 16 stroke:#0144cb,color:#0144cb
			task_30b6fb74["[task]  Save the join command to a file"]
			style task_30b6fb74 stroke:#0144cb,fill:#ffffff
			play_f989ecef --> |"4"| task_30b6fb74
			linkStyle 17 stroke:#0144cb,color:#0144cb
		%% End of the play 'Play: Initialize Kubernetes master node (0)'
		%% Start of the play 'Play: Join worker nodes to the cluster (0)'
		play_f27b1fc5["Play: Join worker nodes to the cluster (0)"]
		style play_f27b1fc5 fill:#57b11b,color:#ffffff
		playbook_70a5566f --> |"3"| play_f27b1fc5
		linkStyle 18 stroke:#57b11b,color:#57b11b
			task_1473ce94["[task]  Fetch join command from master"]
			style task_1473ce94 stroke:#57b11b,fill:#ffffff
			play_f27b1fc5 --> |"1"| task_1473ce94
			linkStyle 19 stroke:#57b11b,color:#57b11b
			task_6241eddc["[task]  Join worker nodes to the Kubernetes cluster"]
			style task_6241eddc stroke:#57b11b,fill:#ffffff
			play_f27b1fc5 --> |"2"| task_6241eddc
			linkStyle 20 stroke:#57b11b,color:#57b11b
		%% End of the play 'Play: Join worker nodes to the cluster (0)'
	%% End of the playbook 'setup-k8s.yaml'

