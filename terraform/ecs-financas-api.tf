# ==========================================================
# 1. IAM ROLES
# ==========================================================

# A. Role de Execução (Infraestrutura)
resource "aws_iam_role" "role_task_execution_financas_api" {
  name = "role-task-execution-financas-api-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

# Política de Execução Consolidada (ECR + Logs + Secrets)
resource "aws_iam_policy" "policy_task_execution_financas_api" {
  name        = "policy-task-execution-financas-api-${var.ambiente}"
  description = "Permissoes para o ECS puxar imagens do ECR, gravar logs e ler secrets"

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
        Action = [
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = "*"
      },
      {
        Effect   = "Allow"
        Action   = [
          "secretsmanager:GetSecretValue"
        ]
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_execution_standard_api" {
  role       = aws_iam_role.role_task_execution_financas_api.name
  policy_arn = aws_iam_policy.policy_task_execution_financas_api.arn
}

resource "aws_iam_role_policy_attachment" "role_policy_ecs_task_execution_managed_api" {
  role       = aws_iam_role.role_task_execution_financas_api.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# B. Task Role (Aplicação - S3)
resource "aws_iam_role" "role_task_financas_api" {
  name = "role-task-financas-api-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{ Action = "sts:AssumeRole", Effect = "Allow", Principal = { Service = "ecs-tasks.amazonaws.com" } }]
  })
}

resource "aws_iam_policy" "policy_task_financas_api" {
  name = "policy-task-financas-api-${var.ambiente}"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow",
        Action = ["s3:*"],
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_task_resources_api" {
  role       = aws_iam_role.role_task_financas_api.name
  policy_arn = aws_iam_policy.policy_task_financas_api.arn
}

# ==========================================================
# 2. RECURSOS DO SERVIÇO
# ==========================================================

resource "aws_security_group" "sg_ecs_financas_api" {
  name        = "ecs-financas-api-sg-${var.ambiente}"
  description = "Permite trafego para o servico de api"
  vpc_id      = aws_vpc.vpc_financas.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    security_groups = [aws_security_group.sg_alb_financas_api.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, { Name = "ecs-financas-api-sg-${var.ambiente}" })
}

resource "aws_cloudwatch_log_group" "log_group_ecs_financas_api" {
  name              = "/ecs/financas-api-${var.ambiente}"
  retention_in_days = 7
}

resource "aws_ecs_task_definition" "ecs_task_definition_financas_api" {
  family                   = "ecs-financas-api-${var.ambiente}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.role_task_execution_financas_api.arn
  task_role_arn            = aws_iam_role.role_task_financas_api.arn

  container_definitions = jsonencode([
    {
      name      = "financas-api"
      image     = "841816327169.dkr.ecr.${var.aws_region}.amazonaws.com/${var.ecr_financas_api}:latest"
      essential = true

      portMappings = [{
        containerPort = 8080,
        hostPort = 8080,
        protocol = "tcp"
      }]

      secrets = [
        {
          name      = "AWS_RDS_PASSWORD"
          valueFrom = aws_secretsmanager_secret.rds_password.arn
        },
        {
          name      = "AWS_RDS_URL"
          valueFrom = aws_secretsmanager_secret.rds_url.arn
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = "/ecs/financas-api-${var.ambiente}"
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }

    }
  ])
}

resource "aws_ecs_service" "ecs_service_financas_api" {
  name            = "ecs-financas-api-service"
  cluster         = aws_ecs_cluster.ecs_cluster_financas.id
  task_definition = aws_ecs_task_definition.ecs_task_definition_financas_api.arn
  desired_count   = var.ecs_tasks_desejadas

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 1
  }

  network_configuration {
    subnets          = [
      aws_subnet.subnet_financas_publica_az_a.id,
      aws_subnet.subnet_financas_publica_az_b.id
    ]
    security_groups  = [aws_security_group.sg_ecs_financas_api.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg_financas_api.arn
    container_name   = "financas-api"
    container_port   = 8080
  }
}