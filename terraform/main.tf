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

data "aws_vpc" "default" {
  default = true
}

# Njibou ken el subnets elli f'zones maqboulin men EKS (bla us-west-2d)
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
  filter {
    name   = "availability-zone"
    values = ["us-west-2a", "us-west-2b", "us-west-2c"]
  }
}

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

# 2. Nasn3ou el Machines (Nodes)
resource "aws_eks_node_group" "student_nodes" {
  cluster_name    = aws_eks_cluster.student_cluster.name
  node_group_name = "student-nodes"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = slice(data.aws_subnets.default.ids, 0, 2)
  instance_types  = ["t3.small"]

  scaling_config {
    desired_size = 1
    max_size     = 2
    min_size     = 1
  }

  depends_on = [aws_eks_cluster.student_cluster]
}