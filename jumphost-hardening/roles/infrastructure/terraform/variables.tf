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
  description = "The cidr_block to set in public subnet- Replaced by Ansible Playbook on run"
}

variable "ip_adresses" {
  type = list(string)
  description = "The ip addresses allowed for ssh access- Replaced by Ansible Playbook on run"
}

variable "app" {
  description = "the name of your stack, e.g. \"demo\""
}

variable "environment" {
  type = string
  description = "the name of your environment, e.g. \"prod\""
}

variable "instance_type" {
  type = string
  description = "The instancetype to use"
}

variable "ami_bastion" {
  type = string
  description = "The ami to use for the bastion"
}

variable "ami_webserver" {
  type = string
  description = "The ami to use for the webserver"
}