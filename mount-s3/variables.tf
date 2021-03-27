variable "credentialFile" {
  type    = string
  default = "/home/lukas/.aws/credentials"
}

variable "region" {
  type    = string
  default = "eu-central-1"
}
variable "projectTagValue" {
  type    = string
  default = "MultitierApp"
}

variable "bucketName" {
  type    = string
  default = "multitier-kienast"
}