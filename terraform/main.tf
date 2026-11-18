terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-west-2"
}

# Njibou el VPC w el Subnets el mawjoudin par défaut f'AWS Academy
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Njibou el LabRole el jahez mta3 AWS Academy
data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

# 1. Nasn3ou el Cluster EKS
resource "aws_eks_cluster" "student_cluster" {
  name     = "student-eks-cluster"
  role_arn = data.aws_iam_role.lab_role.arn

  vpc_config {
    subnet_ids = slice(data.aws_subnets.default.ids, 0, 2)
  }
}

# 2. Nasn3ou el Machines (Nodes) elli bech y'dour 3lihom Kubernetes
resource "aws_eks_node_group" "student_nodes" {
  cluster_name    = aws_eks_cluster.student_cluster.name
  node_group_name = "student-nodes"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = slice(data.aws_subnets.default.ids, 0, 2)
  instance_types  = ["t3.medium"]

  scaling_config {
    desired_size = 1
    max_size     = 2
    min_size     = 1
  }

  depends_on = [aws_eks_cluster.student_cluster]
}