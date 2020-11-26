provider "aws" {
  region                  = var.region
  shared_credentials_file = var.credentialFile
  profile                 = "default"
}

#Call the identity to use the accountId
data "aws_caller_identity" "current" {}

#DynamoDb for storing mailaddresses
resource "aws_dynamodb_table" "subscriber_table" {
  name           = "${terraform.workspace}-${var.tableName}"
  billing_mode   = "PROVISIONED"
  read_capacity  = 5
  write_capacity = 5
  hash_key       = "Mailaddress"

  attribute {
    name = "Mailaddress"
    type = "S"
  }

  ttl {
    attribute_name = "TimeToExist"
    enabled        = false
  }

  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}

#SQS for storing the users which should receive the mail
resource "aws_sqs_queue" "subscriber_queue" {
  name                              = "${terraform.workspace}-${var.queueName}"
  max_message_size                  = 262144
  message_retention_seconds         = 86400
  kms_master_key_id                 = "alias/aws/sqs"
  kms_data_key_reuse_period_seconds = 300

  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}

#Role which retrieves the users from dynamodb and add to sqs
resource "aws_iam_role" "iam_for_dynamolambda" {
  name = "${terraform.workspace}-iam_for_dynamolambda"

  assume_role_policy = <<EOF
{
	"Version": "2012-10-17",
	"Statement": [
    {
        "Action": "sts:AssumeRole",
        "Principal": {
            "Service": "lambda.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
    }
	]
}
EOF
  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}


resource "aws_iam_role_policy" "dynamo_policy" {
  name = "${terraform.workspace}-dynamo_policy"
  role = aws_iam_role.iam_for_dynamolambda.id

  policy = <<-EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
            "dynamodb:Scan"
        ],
        "Resource": "${aws_dynamodb_table.subscriber_table.arn}"
      },
      {
        "Effect": "Allow",
        "Action": [
            "sqs:GetQueueUrl",
            "sqs:SendMessage"
        ],
        "Resource": "${aws_sqs_queue.subscriber_queue.arn}"
      },
      {
          "Effect": "Allow",
          "Action": [
              "logs:CreateLogStream",
              "logs:PutLogEvents"
          ],
          "Resource": "arn:aws:logs:${var.region}:${data.aws_caller_identity.current.account_id}:*"
      },
      {
          "Effect": "Allow",
          "Action": [
              "logs:CreateLogGroup",
              "kms:Decrypt"
          ],
          "Resource": "*"
        }
    ]
  }
  EOF
}



#Lambda Function which adds the users in db to sqs
resource "aws_lambda_function" "dynamo_lambda" {
  filename      = "dynamo-function.zip"
  function_name = "${terraform.workspace}-${var.dynamoFunctionName}"
  role          = aws_iam_role.iam_for_dynamolambda.arn
  memory_size   = 384
  handler       = "lambda_function.lambda_handler"
  depends_on = [
    aws_cloudwatch_log_group.dynamo_log,
  ]

  # The filebase64sha256() function is available in Terraform 0.11.12 and later
  # For Terraform 0.11.11 and earlier, use the base64sha256() function and the file() function:
  # source_code_hash = "${base64sha256(file("lambda_function_payload.zip"))}"
  source_code_hash = filebase64sha256("dynamo-function.zip")

  runtime = "python3.8"

  environment {
    variables = {
      tableName = "${terraform.workspace}-${var.tableName}",
      region    = var.region,
      queueName = "${terraform.workspace}-${var.queueName}"
    }
  }
  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}

#Loggroup for dynamodb
resource "aws_cloudwatch_log_group" "dynamo_log" {
  name              = "/aws/lambda/${terraform.workspace}-${var.dynamoFunctionName}"
  retention_in_days = 7

  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}




#Role which retrieves the sqs and adds to ses
resource "aws_iam_role" "iam_for_sqslambda" {
  name = "${terraform.workspace}-iam_for_sqslambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}


resource "aws_iam_role_policy" "sqs_policy" {
  name = "${terraform.workspace}-sqs_policy"
  role = aws_iam_role.iam_for_sqslambda.id

  policy = <<-EOF
  {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sqs:ReceiveMessage",
                "sqs:DeleteMessage",
                "sqs:GetQueueAttributes"
            ],
            "Resource": "${aws_sqs_queue.subscriber_queue.arn}"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": "arn:aws:logs:${var.region}:${data.aws_caller_identity.current.account_id}:*"
        },
        {
          "Effect": "Allow",
          "Action": [
              "logs:CreateLogGroup",
              "kms:Decrypt"
          ],
          "Resource": "*"
        }
    ]
  }
  EOF
}

#Lambda Function which adds to ses
resource "aws_lambda_function" "sqs_lambda" {
  filename      = "sqs-function.zip"
  function_name = "${terraform.workspace}-${var.sqsFunctionName}"
  role          = aws_iam_role.iam_for_sqslambda.arn
  memory_size   = 128
  handler       = "lambda_function.lambda_handler"
  depends_on = [
    aws_cloudwatch_log_group.sqs_log,
  ]

  # The filebase64sha256() function is available in Terraform 0.11.12 and later
  # For Terraform 0.11.11 and earlier, use the base64sha256() function and the file() function:
  # source_code_hash = "${base64sha256(file("lambda_function_payload.zip"))}"
  source_code_hash = filebase64sha256("sqs-function.zip")

  runtime = "python3.8"

  environment {
    variables = {
      senderName = var.mailSender,
      region     = var.region,
      queueName  = "${terraform.workspace}-${var.queueName}"
    }
  }
  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}



#Loggroup for sqsdb
resource "aws_cloudwatch_log_group" "sqs_log" {
  name              = "/aws/lambda/${terraform.workspace}-${var.sqsFunctionName}"
  retention_in_days = 7

  tags = {
    project     = var.projectTagValue
    environment = terraform.workspace
  }
}


#The Trigger on when to add the users from dynamodb to sqs
resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = aws_sqs_queue.subscriber_queue.arn
  function_name    = aws_lambda_function.sqs_lambda.arn
}


resource "aws_lambda_permission" "with_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.dynamo_lambda.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = aws_sns_topic.newsletter_topic.arn
}

resource "aws_sns_topic" "newsletter_topic" {
  name = "${terraform.workspace}-${var.snsName}"
  kms_master_key_id = "alias/aws/sns"
}


resource "aws_sns_topic_subscription" "newsletter_topic_subscription" {
  topic_arn = aws_sns_topic.newsletter_topic.arn
  protocol  = "lambda"
  endpoint  = aws_lambda_function.dynamo_lambda.arn
}

