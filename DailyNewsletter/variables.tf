variable "credentialFile" {
  type = string
  default = "/home/lukas/.aws/credentials"
}

variable "accountId" {
  type = string
  default = "680785598240"
}

variable "region" {
  type = string
  default = "eu-central-1"
}

variable "projectTagValue" {
  type = string
  default = "Newsletter"
}


variable "tableName" {
  type = string
  default = "subscribetable"
}


variable "queueName" {
  type = string
  default = "subscribequeue"
}

variable "mailSender" {
  type = string
  default = "test@example.com"
}

