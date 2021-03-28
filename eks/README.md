# eks with ansible


# Infrastructure

<code>ansible-playbook -i hosts infrastructure.yaml</code><br/>
Creates an eks cluster with eksctl for usage.
After starting the command get a coffee since it will need some time


# Kube
<code>ansible-playbook -i hosts kube.yaml</code><br/>
Install metricsserver
Installs jupyterhub on the eks cluster for showcasing