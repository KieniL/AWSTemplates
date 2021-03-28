# eks with ansible


# Infrastructure

<code>ansible-playbook -i hosts infrastructure.yaml</code><br/>
Creates an eks cluster with eksctl for usage.
After starting the command get a coffee since it will need some time

If you need to scale change the node sizes and then rerun the playbook again
If you need to change instancetype you have to delete the cluster and create a new one.

# Kube
<code>ansible-playbook -i hosts kube.yaml</code><br/>
Install metricsserver
Installs jupyterhub on the eks cluster for showcasing

You then need to create an ingress for jupyterhub since it is a clusterIp by default