# Create a VPC
resource "aws_vpc" "vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  instance_tenancy     = "default"
  tags                 = var.tags
}

#Create a Subnet
resource "aws_subnet" "subnet1" {
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = var.az1
  map_public_ip_on_launch = false
  tags                    = var.tags
}
#Create a Subnet
resource "aws_subnet" "subnet2" {
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = var.az2
  map_public_ip_on_launch = false
  tags                    = var.tags
}

#Create an Internet Gateway
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.vpc.id
}

#Create a Route Table
resource "aws_route_table" "main" {
  vpc_id = aws_vpc.vpc.id
  tags   = var.tags
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
}

resource "aws_route_table_association" "rt_subnet1" {
  subnet_id      = aws_subnet.subnet1.id
  route_table_id = aws_route_table.main.id
}

resource "aws_route_table_association" "rt_subnet2" {
  subnet_id      = aws_subnet.subnet2.id
  route_table_id = aws_route_table.main.id
}

resource "aws_autoscaling_group" "autoscaling" {
  name     = "${var.clusterName}-autoscaling"
  max_size = var.maxCount
  min_size = var.minCount

  tag {
    key                 = "AmazonECSManaged"
    value               = ""
    propagate_at_launch = true
  }
}

resource "aws_ecs_capacity_provider" "ecs_capacity" {
  name = "${var.clusterName}-ecscapacity"

  auto_scaling_group_provider {
    auto_scaling_group_arn         = aws_autoscaling_group.autoscaling.arn
    managed_termination_protection = "ENABLED"

    managed_scaling {
      maximum_scaling_step_size = 10
      minimum_scaling_step_size = 1
      status                    = "ENABLED"
      target_capacity           = var.desiredCount
    }
  }
}


resource "aws_ecs_cluster" "ecscluster" {
  name = var.clusterName

  capacity_providers = aws_ecs_capacity_provider.ecs_capacity[*].name

  tags = var.tags
}


resource "aws_ecs_service" "backend" {
  name            = var.serviceName
  cluster         = aws_ecs_cluster.ecscluster.id
  task_definition = aws_ecs_task_definition.task.arn
  desired_count   = var.desiredCount
  iam_role        = aws_iam_role.ecs_role.arn

  ordered_placement_strategy {
    type  = "binpack"
    field = "cpu"
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.alb_target_group.arn
    container_name   = "${var.clusterName}-container"
    container_port   = 8080
  }

  placement_constraints {
    type       = "memberOf"
    expression = "attribute:ecs.availability-zone in [eu-central-1a, eu-central-1b, eu-central-1c]"
  }

  tags = var.tags
}

resource "aws_ecs_task_definition" "task" {
  family                = var.taskName
  container_definitions = <<DEFINITIONS
[
  {
    "cpu": 128,
    "essential": true,
    "image": "${var.image}",
    "memory": 128,
    "name": "${var.clusterName}-container",
    "portMappings": [
      {
        "containerPort": 8080
      }
    ]
  }
]
DEFINITIONS

  volume {
    name      = "service-storage"
    host_path = "/ecs/service-storage"
  }

  placement_constraints {
    type       = "memberOf"
    expression = "attribute:ecs.availability-zone in [eu-central-1a, eu-central-1b, eu-central-1c]"
  }
}


resource "aws_iam_role_policy_attachment" "ecs_role_policy_attachment" {
  role       = aws_iam_role.ecs_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceRole"
}

resource "aws_iam_role" "ecs_role" {
  name = "${var.clusterName}-role"

  assume_role_policy = <<EOF
{
    "Version": "2008-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "ecs.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
      }
    ]
}
EOF
}


resource "aws_security_group" "ecs_alb" {
  description = "Balancer for ${var.clusterName}"

  vpc_id = aws_vpc.vpc.id
  name   = "${var.clusterName}-alb-sg"

  ingress {
    protocol    = "tcp"
    from_port   = "8080"
    to_port     = "8080"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_alb" "alb" {
  name = "${var.clusterName}-alb"
  subnets = [
    aws_subnet.subnet1.id,
    aws_subnet.subnet2.id
  ]
  security_groups = [
    aws_security_group.ecs_alb.id
  ]
}


resource "aws_alb_target_group" "alb_target_group" {
  name     = "${var.clusterName}-tg"
  port     = "8080"
  protocol = "HTTP"
  vpc_id   = aws_vpc.vpc.id

  depends_on = [
    aws_alb.alb
  ]
}

resource "aws_alb_listener" "http" {
  count             = "${var.https ? 0 : 1}"
  load_balancer_arn = "${aws_alb.alb.id}"
  port              = "8080"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.alb_target_group.id}"
    type             = "forward"
  }
}
