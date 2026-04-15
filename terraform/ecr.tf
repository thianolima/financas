# Repositorio Financas Despesas Integrador
resource "aws_ecr_repository" "ecr_financas_despesas_integrador" {
  name = var.ecr_financas_despesas_integrador
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = var.common_tags
}

resource "aws_ecr_lifecycle_policy" "policy_ecr_financas_despesas_integrador" {
  repository = aws_ecr_repository.ecr_financas_despesas_integrador.name

  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "Manter apenas as últimas 3 imagens para economizar espaço"
      selection = {
        tagStatus     = "any"
        countType     = "imageCountMoreThan"
        countNumber   = 3
      }
      action = {
        type = "expire"
      }
    }]
  })
}

# Repositorio Financas Despesas Processador
resource "aws_ecr_repository" "ecr_financas_despesas_processador" {
  name = var.ecr_financas_despesas_processador
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = var.common_tags
}

resource "aws_ecr_lifecycle_policy" "policy_ecr_financas_despesas_processador" {
  repository = aws_ecr_repository.ecr_financas_despesas_processador.name

  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "Manter apenas as últimas 3 imagens para economizar espaço"
      selection = {
        tagStatus     = "any"
        countType     = "imageCountMoreThan"
        countNumber   = 3
      }
      action = {
        type = "expire"
      }
    }]
  })
}

# Repositorio Financas API
resource "aws_ecr_repository" "ecr_financas_api" {
  name = var.ecr_financas_api
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = var.common_tags
}

resource "aws_ecr_lifecycle_policy" "policy_ecr_financas_api" {
  repository = aws_ecr_repository.ecr_financas_api.name

  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "Manter apenas as últimas 3 imagens para economizar espaço"
      selection = {
        tagStatus     = "any"
        countType     = "imageCountMoreThan"
        countNumber   = 3
      }
      action = {
        type = "expire"
      }
    }]
  })
}