// DO NOT EDIT - ANSIBLE replace the variables

variable "region" {
  type = string
  description = "The region to deploy to- Replaced by Ansible Playbook on run"
}

variable "cidr_block" {
  type = string
  description = "The cidr_block to set in VPC- Replaced by Ansible Playbook on run"
}

variable "subnet_a_cidr_block" {
  type = string
  description = "The cidr_block to set in subnet a- Replaced by Ansible Playbook on run"
}

variable "subnet_b_cidr_block" {
  type = string
  description = "The cidr_block to set in subnet b- Replaced by Ansible Playbook on run"
}

variable "local_ip" {
  type = string
  description = "The local_ip- Replaced by Ansible Playbook on run"
}

variable "project_name" {
  type = string
  description = "The projectname for tagging for the resources- Replaced by Ansible Playbook on run"
}
