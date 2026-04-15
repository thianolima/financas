# ==========================================================
# 1. IAM ROLES (Identidades)
# ==========================================================

# A. Role de Execução (Infraestrutura)
resource "aws_iam_role" "role_task_execution_financas_desepesas_integrador" {
  name = "role-task-execution-financas-despesas-integrador-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17" # Adicionado para conformidade
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

# Política de Execução Consolidada (ECR + Logs)
resource "aws_iam_policy" "policy_task_execution_financas_despesas_integrador" {
  name        = "policy-task-execution-financas-despesas-integrador-${var.ambiente}"
  description = "Permissoes para o ECS puxar imagens do ECR e gravar logs"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage"
        ]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = ["logs:CreateLogStream", "logs:PutLogEvents"]
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_execution_standard" {
  role       = aws_iam_role.role_task_execution_financas_desepesas_integrador.name
  policy_arn = aws_iam_policy.policy_task_execution_financas_despesas_integrador.arn
}

resource "aws_iam_role_policy_attachment" "role_policy_ecs_task_execution_managed" {
  role       = aws_iam_role.role_task_execution_financas_desepesas_integrador.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# B. Task Role (Aplicação - S3/SQS)
resource "aws_iam_role" "role_task_financas_despesas_integrador" {
  name = "role-task-financas-despesas-integrador-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{ Action = "sts:AssumeRole", Effect = "Allow", Principal = { Service = "ecs-tasks.amazonaws.com" } }]
  })
}

resource "aws_iam_policy" "policy_task_financas_despesas_integrador" {
  name = "policy-task-financas-despesas-integrador-${var.ambiente}"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow",
      Action = ["sqs:*", "s3:*"],
      Resource = "*"
    }]
  })
}

# CORREÇÃO DA REFERÊNCIA ABAIXO:
resource "aws_iam_role_policy_attachment" "attach_task_resources" {
  role       = aws_iam_role.role_task_financas_despesas_integrador.name # Nome corrigido
  policy_arn = aws_iam_policy.policy_task_financas_despesas_integrador.arn
}

# ==========================================================
# 2. RECURSOS DO SERVIÇO
# ==========================================================

resource "aws_security_group" "sg_ecs_financas_despesa_integrador" {
  name        = "ecs-financas-despesas-integrador-sg-${var.ambiente}"
  description = "Permite trafego para o servico de integracao"
  vpc_id      = aws_vpc.vpc_financas.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["10.0.1.0/24"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, { Name = "ecs-financas-despesas-integrador-sg-${var.ambiente}" })
}

resource "aws_cloudwatch_log_group" "log_group_ecs_financas_despesas_integrador" {
  name              = "/ecs/financas-despesas-integrador-${var.ambiente}"
  retention_in_days = 7
}

resource "aws_ecs_task_definition" "ecs_task_definition_financas_despesa_integrador" {
  family                   = "ecs-financas-despesas-integrador-${var.ambiente}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.role_task_execution_financas_desepesas_integrador.arn
  task_role_arn            = aws_iam_role.role_task_financas_despesas_integrador.arn

  container_definitions = jsonencode([
    {
      name      = "financas-despesas-integrador"
      image     = "841816327169.dkr.ecr.${var.aws_region}.amazonaws.com/${var.ecr_financas_despesas_integrador}:latest"
      essential = true
      portMappings = [{ containerPort = 8080, hostPort = 8080, protocol = "tcp" }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = "/ecs/financas-despesas-integrador-${var.ambiente}"
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ecs_service_financas_integrador" {
  name            = "ecs-financas-despesas-integrador-service"
  cluster         = aws_ecs_cluster.ecs_cluster_financas.id
  task_definition = aws_ecs_task_definition.ecs_task_definition_financas_despesa_integrador.arn
  desired_count   = 1

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 1
  }

  network_configuration {
    subnets          = [aws_subnet.subnet_financas_publica.id]
    security_groups  = [aws_security_group.sg_ecs_financas_despesa_integrador.id]
    assign_public_ip = true
  }
}