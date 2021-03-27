variable "region" {
  type = string
  description = "The region to deploy to- Replaced by Ansible Playbook on run"
}

variable "cidr_block" {
  type = string
  description = "The cidr_block to set in VPC- Replaced by Ansible Playbook on run"
}

variable "subnet_public_cidr_block" {
  type = string
  description = "The cidr_block to set in public - Replaced by Ansible Playbook on run"
}

variable "subnet_private_cidr_block" {
  type = string
  description = "The cidr_block to set in private subnet- Replaced by Ansible Playbook on run"
}

variable "cluster_name" {
  description = "the name of your stack, e.g. \"demo\""
}

variable "environment" {
  type = string
  description = "the name of your environment, e.g. \"prod\""
}
