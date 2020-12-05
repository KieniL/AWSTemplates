provider "aws" {
  region                  = var.region
  shared_credentials_file = var.credentialFile
  profile                 = "default"
}


#Call the identity to use the accountId
data "aws_caller_identity" "current" {}



module "website_s3_bucket" {
  source = "./modules/s3"

  bucketName = "${terraform.workspace}-${var.bucketName}"

  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}


module "ecs" {
  source = "./modules/ecs"

  clusterName  = "${terraform.workspace}-${var.clusterName}"
  serviceName  = "${terraform.workspace}-${var.serviceName}"
  taskName     = "${terraform.workspace}-${var.taskName}"
  desiredCount = var.desiredCount
  https        = var.https
  image        = var.image
  minCount     = var.minCount
  maxCount     = var.maxCount
  az1          = var.az1
  az2          = var.az2
  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}
