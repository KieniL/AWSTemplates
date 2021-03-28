provider "aws" {
  region = var.region
}

resource "aws_vpc" "main" {
  cidr_block           = var.cidr_block
  enable_dns_hostnames = true


  tags = {
    Name    = "${var.app}-${var.environment}-vpc"
    project = "${var.app}-${var.environment}"
  }
}

resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = var.subnet_public_cidr_block
  availability_zone       = "${var.region}a"
  map_public_ip_on_launch = true
  depends_on              = [aws_vpc.main]

  tags = {
    project = "${var.app}-${var.environment}"
    state   = "public"
  }
}


resource "aws_internet_gateway" "gw" {
  vpc_id     = aws_vpc.main.id
  depends_on = [aws_vpc.main]

  tags = {
    Name    = "internet-gateway-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
}



resource "aws_route_table" "internet-route" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
  depends_on = [aws_vpc.main]
  tags = {
    Name    = "public_route_table-${var.environment}"
    state   = "public"
    project = "${var.app}-${var.environment}"
  }
}


resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.internet-route.id

  depends_on = [aws_route_table.internet-route,
    aws_subnet.public
  ]
}


# Create the PublicSecurityGroup
resource "aws_security_group" "bastion" {
  name        = "BastionSecurityGroup"
  description = "Allow ssh connection from client"
  vpc_id      = aws_vpc.main.id
  tags = {
    Name    = "bastionsecuritygroup-${var.environment}"
    project = "${var.app}-${var.environment}"
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = var.ip_adresses
  }
}

# Create the PublicSecurityGroup
resource "aws_security_group" "web" {
  name        = "WebServerSecurityGroup"
  description = "Allow http connection from client"
  vpc_id      = aws_vpc.main.id
  tags = {
    Name    = "websecuritygroup-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = [
      "${aws_instance.bastion.private_ip}/32"
    ]
    security_groups = [
      aws_security_group.bastion.id
    ]
  }
}


resource "aws_iam_role" "main" {
  name               = "webserverrole"
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
    Name    = "webserverrole-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
}

#Create instance profile
resource "aws_iam_instance_profile" "main" {
  name = "instanceprofile"
  role = aws_iam_role.main.name
}

#create webServer
resource "aws_instance" "webserver" {
  ami                    = var.ami_webserver
  iam_instance_profile   = aws_iam_instance_profile.main.name
  subnet_id              = aws_subnet.public.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.web.id]
  key_name               = var.key_name
  tags = {
    Name    = "webserver-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
  user_data = <<EOF
#/bin/sh
sudo apt-get update && sudo apt-get install -y apache2
sudo service apache2 start
echo "<html><body><h1>Awesome !!!</h1>" > /var/www/html/index.html
echo "</body></html>" >> /var/www/html/index.html
sudo service apache2 reload
EOF
}


#create bastioinhost
resource "aws_instance" "bastion" {
  ami                    = var.ami_bastion
  iam_instance_profile   = aws_iam_instance_profile.main.name
  subnet_id              = aws_subnet.public.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.bastion.id]
  key_name               = var.key_name
  tags = {
    Name    = "bastion-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
  user_data = <<EOF
#/bin/sh
echo "Test"
EOF
}

