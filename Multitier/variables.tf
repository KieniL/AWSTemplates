variable "credentialFile" {
  type    = string
  default = "/home/lukas/.aws/credentials"
}

variable "region" {
  type    = string
  default = "eu-central-1"
}
variable "az1" {
  default = "eu-central-1a"
  type    = string
}

variable "az2" {
  default = "eu-central-1b"
  type    = string
}

variable "projectTagValue" {
  type    = string
  default = "MultitierApp"
}

variable "bucketName" {
  type    = string
  default = "multitier-kienast"
}

variable "clusterName" {
  default = "multitier-cluster"
  type    = string
}

variable "serviceName" {
  default = "multitier-service"
  type    = string
}

variable "taskName" {
  default = "multitier-task"
  type    = string
}

variable "desiredCount" {
  default = 1
  type    = number
}
variable "minCount" {
  default = 1
  type    = number
}

variable "maxCount" {
  default = 3
  type    = number
}

variable "image" {
  default = "luke19/lernplattform-backend:latest"
  type    = string
}


variable "https" {
  type    = bool
  default = false
}


