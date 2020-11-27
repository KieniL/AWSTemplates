provider "aws" {
  region                  = var.region
  shared_credentials_file = var.credentialFile
  profile                 = "default"
}


#Call the identity to use the accountId
data "aws_caller_identity" "current" {}


resource "aws_s3_bucket" "website_bucket" {
  bucket = var.bucketName
  acl = "public-read"
  policy = data.aws_iam_policy_document.website_policy.json
  website {
    index_document = "index.html"
    error_document = "index.html"
  }
}


data "aws_iam_policy_document" "website_policy" {
  statement {
    actions = [
      "s3:GetObject"
    ]
    principals {
      identifiers = ["*"]
      type = "AWS"
    }
    resources = [
      "arn:aws:s3:::${var.bucketName}/*"
    ]
  }
}

