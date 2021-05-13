# jumphost hardening (In Progress)

An example app with a bastion in a public subnet and a webserver also in public subnet

The bastion is secured by multiple things:
1. allowing ssh access only to a defined list of ip adresses
2. Only allow ssh to secure hosts from the jumphost
3. Change ssh port on jumphost to the port defined
4. Add and configure endlessh on Port 22 to tricker Hackers


Creates a ssh_config with the parameters defined. You can then use it with ssh -F $SSH_CONFIG_FILE jumphost