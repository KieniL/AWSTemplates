provider "aws" {
  region                  = var.region
  shared_credentials_file = var.credentialFile
  profile                 = "default"
}

#DynamoDb for storing mailaddresses
resource "aws_dynamodb_table" "subscriber_table" {
  name           = var.tableName
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
    project = var.projectTagValue
  }
}

#SQS for storing the users which should receive the mail
resource "aws_sqs_queue" "subscriber_queue" {
  name                      = var.queueName
  max_message_size          = 262144
  message_retention_seconds = 86400
  kms_master_key_id         = "alias/aws/sqs"
  kms_data_key_reuse_period_seconds = 300

  tags = {
    project = var.projectTagValue
  }
}

#Role which retrieves the users from dynamodb and add to sqs
resource "aws_iam_role" "iam_for_dynamolambda" {
  name = "iam_for_dynamolambda"

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
    project = var.projectTagValue
  }
}


resource "aws_iam_role_policy" "dynamo_policy" {
  name = "dynamo_policy"
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
          "Resource": "arn:aws:logs:${var.region}:${var.accountId}:*"
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
  function_name = var.dynamoFunctionName
  role          = aws_iam_role.iam_for_dynamolambda.arn
  memory_size   = 128
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
      tableName = var.tableName,
      region    = var.region,
      queueName = var.queueName
    }
  }
  tags = {
    project = var.projectTagValue
  }
}

#Loggroup for dynamodb
resource "aws_cloudwatch_log_group" "dynamo_log" {
  name              = "/aws/lambda/${var.dynamoFunctionName}"
  retention_in_days = 7

  tags = {
    project = var.projectTagValue
  }
}




#Role which retrieves the sqs and adds to ses
resource "aws_iam_role" "iam_for_sqslambda" {
  name = "iam_for_sqslambda"

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
    project = var.projectTagValue
  }
}


resource "aws_iam_role_policy" "sqs_policy" {
  name = "sqs_policy"
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
            "Resource": "arn:aws:logs:${var.region}:${var.accountId}:*"
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
  function_name = var.sqsFunctionName
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
      region    = var.region,
      queueName = var.queueName
    }
  }
  tags = {
    project = var.projectTagValue
  }
}



#Loggroup for sqsdb
resource "aws_cloudwatch_log_group" "sqs_log" {
  name              = "/aws/lambda/${var.sqsFunctionName}"
  retention_in_days = 7

  tags = {
    project = var.projectTagValue
  }
}


#The Trigger on when to add the users from dynamodb to sqs
resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = aws_sqs_queue.subscriber_queue.arn
  function_name    = aws_lambda_function.sqs_lambda.arn
}

