# Define o provedor AWS
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# Configura o provedor AWS
provider "aws" {
  region = var.aws_region
}

# Recupera o ID da conta AWS em tempo de execução
data "aws_caller_identity" "current" {}

