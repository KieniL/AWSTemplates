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
  default = "Newsletter"
}


variable "tableName" {
  type    = string
  default = "subscribetable"
}


variable "queueName" {
  type    = string
  default = "subscribequeue"
}

variable "mailSender" {
  type    = string
  default = "lukaskienast0@gmail.com"
}


variable "dynamoFunctionName" {
  type    = string
  default = "dynamo-function"
}

variable "sqsFunctionName" {
  type    = string
  default = "sqs-function"
}
