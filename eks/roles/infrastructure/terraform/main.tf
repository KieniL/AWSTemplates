
provider "aws" {
  region = var.region
}

 resource "aws_vpc" "main" {
  cidr_block           = var.cidr_block
  enable_dns_hostnames = true
  

  tags = {
    Name = "${var.cluster_name}-${var.environment}-vpc"
    project     = "${var.cluster_name}-${var.environment}"
  }
}

resource "aws_subnet" "public" {
  vpc_id                   = aws_vpc.main.id
  cidr_block               = var.subnet_public_cidr_block
  availability_zone        = "${var.region}a"
  map_public_ip_on_launch  = true
  depends_on = [ aws_vpc.main ]
  
  tags = {
    project     = "${var.cluster_name}-${var.environment}"
      "kubernetes.io/cluster/${var.cluster_name}-${var.environment}" = "shared"
      "kubernetes.io/role/elb" = 1
    state  = "public"
  }
}



resource "aws_subnet" "private" {
  vpc_id                   = aws_vpc.main.id
  cidr_block               = var.subnet_private_cidr_block
  availability_zone        = "${var.region}b"
  depends_on = [ aws_vpc.main ]
  
  tags = {
    project     = "${var.cluster_name}-${var.environment}"
        "kubernetes.io/cluster/${var.cluster_name}-${var.environment}" = "shared"
        "kubernetes.io/role/internal-elb" = 1
    "state"  = "private"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id
  depends_on = [ aws_vpc.main ]

  tags = {
    Name = "eks-internet-gateway-${var.environment}"
    project     = "${var.cluster_name}-${var.environment}"
  }
}

resource "aws_eip" "nat" {
  vpc              = true
  public_ipv4_pool = "amazon"
}

resource "aws_nat_gateway" "gw" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public.id
  depends_on    = [aws_internet_gateway.gw]

  tags = {
    Name = "eks-nat_Gateway-${var.environment}"
    project     = "${var.cluster_name}-${var.environment}"
  }
}

resource "aws_route_table" "internet-route" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
  depends_on = [ aws_vpc.main ]
  tags  = {
      Name = "eks-public_route_table-${var.environment}"
      state = "public"
      project     = "${var.cluster_name}-${var.environment}"
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
      Name = "eks-nat_route_table-${var.environment}"
      state = "public"
      project     = "${var.cluster_name}-${var.environment}"
  } 
}

resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.internet-route.id
  
  depends_on = [ aws_route_table.internet-route ,
                 aws_subnet.public
  ]
}


resource "aws_route_table_association" "private" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.nat-route.id
  depends_on = [ aws_route_table.nat-route ,
                 aws_subnet.private
  ]
}

resource "aws_eks_cluster" "eks_cluster" {
  name     = "${var.cluster_name}-${var.environment}-cluster"
   
  role_arn = aws_iam_role.eks_cluster_role.arn
  enabled_cluster_log_types = ["api", "audit", "authenticator", "controllerManager", "scheduler"]
  
  tags  = {
      Name = "eks-cluster-${var.environment}"
      project     = "${var.cluster_name}-${var.environment}"
  } 
  
   vpc_config {
    subnet_ids =  [aws_subnet.public.id, aws_subnet.private.id]
  }
   
   timeouts {
     delete    =  "30m"
   }
  
  depends_on = [
    aws_iam_role_policy_attachment.AmazonEKSClusterPolicy1,
    aws_iam_role_policy_attachment.AmazonEKSVPCResourceController1,
    aws_cloudwatch_log_group.cloudwatch_log_group
  ]
}

resource "aws_iam_policy" "AmazonEKSClusterCloudWatchMetricsPolicy" {
  name   = "AmazonEKSClusterCloudWatchMetricsPolicy"
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "cloudwatch:PutMetricData"
            ],
            "Resource": "*",
            "Effect": "Allow"
        }
    ]
}
EOF
}


resource "aws_iam_role" "eks_cluster_role" {
  name = "${var.cluster_name}-cluster-role"
  description = "Allow cluster to manage node groups, fargate nodes and cloudwatch logs"
  force_detach_policies = true
  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "eks.amazonaws.com",
          "eks-fargate-pods.amazonaws.com"
          ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "AmazonEKSClusterPolicy1" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.eks_cluster_role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSCloudWatchMetricsPolicy" {
  policy_arn = aws_iam_policy.AmazonEKSClusterCloudWatchMetricsPolicy.arn
  role       = aws_iam_role.eks_cluster_role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSVPCResourceController1" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
  role       = aws_iam_role.eks_cluster_role.name
}

resource "aws_cloudwatch_log_group" "cloudwatch_log_group" {
  name              = "/aws/eks/${var.cluster_name}-${var.environment}/cluster"
  retention_in_days = 30

  tags = {
    Name        = "${var.cluster_name}-${var.environment}-eks-cloudwatch-log-group"
    project     = "${var.cluster_name}-${var.environment}"
  }
}

resource "aws_eks_fargate_profile" "eks_fargate" {
  cluster_name           = aws_eks_cluster.eks_cluster.name
  fargate_profile_name   = "${var.cluster_name}-${var.environment}-fargate-profile"
  pod_execution_role_arn = aws_iam_role.eks_fargate_role.arn
  subnet_ids             = [aws_subnet.private.id]

  selector {
    namespace = "fargate"
  }

  

  timeouts {
    create   = "30m"
    delete   = "30m"
  }
}

resource "aws_iam_role" "eks_fargate_role" {
  name = "${var.cluster_name}-fargate_cluster_role"
  description = "Allow fargate cluster to allocate resources for running pods"
  force_detach_policies = true
  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "eks.amazonaws.com",
          "eks-fargate-pods.amazonaws.com"
          ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "AmazonEKSFargatePodExecutionRolePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSFargatePodExecutionRolePolicy"
  role       = aws_iam_role.eks_fargate_role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.eks_fargate_role.name
}


resource "aws_iam_role_policy_attachment" "AmazonEKSVPCResourceController" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
  role       = aws_iam_role.eks_fargate_role.name
}


data "tls_certificate" "auth" {
  url = aws_eks_cluster.eks_cluster.identity[0].oidc[0].issuer
}

resource "aws_iam_openid_connect_provider" "main" {
  client_id_list  = ["sts.amazonaws.com"]
  thumbprint_list = [data.tls_certificate.auth.certificates[0].sha1_fingerprint]
  url             = aws_eks_cluster.eks_cluster.identity[0].oidc[0].issuer
}


