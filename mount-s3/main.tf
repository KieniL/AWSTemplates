provider "aws" {
  region                  = var.region
  shared_credentials_file = var.credentialFile
  profile                 = "default"
}

# Create a Role

resource "aws_iam_role" "ec2role" {
  name = "ec2role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF

  tags = {
    project = "BA"
  }
}

#Create instance profile
resource "aws_iam_instance_profile" "main" {
 name = "ec2profile"
 role = aws_iam_role.ec2role.name
}

resource "aws_s3_bucket" "storage" {
  bucket = var.bucketName
  policy = data.aws_iam_policy_document.website_policy.json

  tags = {
    project = "BA"
  }
}


data "aws_iam_policy_document" "storage_policy" {
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