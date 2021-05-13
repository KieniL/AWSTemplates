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

resource "aws_subnet" "private" {
  vpc_id                   = aws_vpc.main.id
  cidr_block               = var.subnet_private_cidr_block
  availability_zone        = "${var.region}b"
  depends_on = [ aws_vpc.main ]

  tags = {
    project     = "${var.app}-${var.environment}"
    "state"  = "private"
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

resource "aws_eip" "nat" {
  vpc              = true
  public_ipv4_pool = "amazon"

  tags = {
    Name = "eipp-${var.environment}"
    project     = "${var.app}-${var.environment}"
  }
}

resource "aws_nat_gateway" "gw" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public.id
  depends_on    = [aws_internet_gateway.gw]

  tags = {
    Name = "nat_Gateway-${var.environment}"
    project     = "${var.app}-${var.environment}"
  }
}


resource "aws_route_table" "nat-route" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_nat_gateway.gw.id
  }
  depends_on = [ aws_vpc.main ]
  tags  = {
      Name = "nat_route_table-${var.environment}"
      state = "public"
      project     = "${var.app}-${var.environment}"
  } 
}

resource "aws_route_table_association" "private" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.nat-route.id
  depends_on = [ aws_route_table.nat-route ,
                 aws_subnet.private
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
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = "${var.ssh_port}"
    to_port   = "${var.ssh_port}"
    protocol  = "tcp"
    cidr_blocks = var.ip_adresses
  }

  egress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = "${var.ssh_port}"
    to_port   = "${var.ssh_port}"
    protocol  = "tcp"
    cidr_blocks = var.ip_adresses
  }

  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Create the WebserverSecurityGroup
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
    security_groups = [
      aws_security_group.bastion.id
    ]
  }

  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
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
  subnet_id              = aws_subnet.private.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.web.id]
  key_name               = var.key_name
  tags = {
    Name    = "webserver-${var.environment}"
    project = "${var.app}-${var.environment}"
  }
  user_data = <<EOF
#!/bin/bash
sudo apt-get update && sudo apt-get install -y apache2
sudo service apache2 start
echo "<html><body><h1>Awesome !!!</h1>" > sudo /var/www/html/index.html
echo "</body></html>" >> sudo /var/www/html/index.html
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
#!/bin/bash

#change ssh port
sudo sed -i "s/#Port 22/Port ${var.ssh_port}/g" /etc/ssh/sshd_config
sudo service sshd restart

# install dependencies
sudo yum update -y && sudo yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
sudo yum update -y && sudo yum install -y git gcc gcc-c++ google-authenticator

# download endlessh
sudo git clone https://github.com/skeeto/endlessh /home/ec2-user/endlessh
cd /home/ec2-user/endlessh && make

#configure endlessh
sudo mv endlessh /usr/local/bin/
sudo cp util/endlessh.service /etc/systemd/system/
sudo systemctl enable endlessh
sudo mkdir -p /etc/endlessh
sudo sh -c  "echo 'Port 22' > /etc/endlessh/config"
sudo systemctl start endlessh
EOF
}

