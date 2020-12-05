variable "clusterName" {
  description = "Name of the ecs cluster."
  type        = string
}

variable "serviceName" {
  description = "Name of the ecs service."
  type        = string
}

variable "taskName" {
  description = "Name of the ecs taskdefinition."
  type        = string
}

variable "desiredCount" {
  description = "number of instances to create."
  type        = number
}
variable "minCount" {
  description =  "number of min instances to create."
  type    = number
}

variable "maxCount" {
  description =  "number of max instances to create."
  type    = number
}
variable "https" {
  description = "Listen over https"
  type        = bool
}

variable "image" {
  description = "the Docker Image name."
  type        = string
}

variable "tags" {
  description = "Tags to set on the cluster."
  type        = map(string)
  default     = {}
}

variable "az1" {
  description = "the first availability zone."
  type        = string
}

variable "az2" {
  description = "the second availability zone."
  type        = string
}

