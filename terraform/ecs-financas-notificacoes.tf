# ==========================================================
# 1. IAM ROLES
# ==========================================================

# A. Role de Execução (Infraestrutura)
resource "aws_iam_role" "role_task_execution_financas_notificacoes" {
  name = "role-task-execution-financas-notificacoes-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

# Política de Deploy (ECR + Logs + Secrets)
resource "aws_iam_policy" "policy_task_execution_financas_notificacoes" {
  name        = "policy-task-execution-financas-notificacoes-${var.ambiente}"
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

# Associação da política à Role de Execução (deploy)
resource "aws_iam_role_policy_attachment" "attach_execution_standard_notificacoes" {
  role       = aws_iam_role.role_task_execution_financas_notificacoes.name
  policy_arn = aws_iam_policy.policy_task_execution_financas_notificacoes.arn
}

resource "aws_iam_role_policy_attachment" "role_policy_ecs_task_execution_managed_notificacoes" {
  role       = aws_iam_role.role_task_execution_financas_notificacoes.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# B. Role da Tarefa (O que o seu código Java pode acessar na AWS)
resource "aws_iam_role" "role_task_financas_notificacoes" {
  name = "role-task-financas-notificacoes-${var.ambiente}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

# Política de recursos da aplicação (Ex: se precisar acessar SQS, Dynamo, etc.)
resource "aws_iam_policy" "policy_task_financas_notificacoes" {
  name = "policy-task-financas-notificacoes-${var.ambiente}"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        "Effect": "Allow",
        "Action": [
          "dynamodb:CreateTable",
          "dynamodb:DeleteTable",
          "dynamodb:DescribeTable",
          "dynamodb:UpdateTable",
          "dynamodb:TagResource",
          "dynamodb:UntagResource",
          "dynamodb:DescribeTimeToLive",
          "dynamodb:UpdateTimeToLive",
          "dynamodb:PutItem",
          "dynamodb:GetItem",
          "dynamodb:UpdateItem",
          "dynamodb:DeleteItem",
          "dynamodb:Query",
          "dynamodb:Scan",
          "dynamodb:DescribeContinuousBackups",
          "dynamodb:ListTagsOfResource"
        ],
        "Resource": [
          "*"
        ]
      },
      {
        Effect = "Allow",
        Action = [
          "sqs:ReceiveMessage",
          "sqs:DeleteMessage",
          "sqs:GetQueueAttributes",
          "sqs:GetQueueUrl",
          "sqs:SendMessage"
        ],
        Resource = [
          "arn:aws:sqs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:sqs-comando-nova-notificacao-${var.ambiente}",
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_task_resources_notificacoes" {
  role       = aws_iam_role.role_task_financas_notificacoes.name
  policy_arn = aws_iam_policy.policy_task_financas_notificacoes.arn
}

# ==========================================================
# 2. RECURSOS DO SERVIÇO
# ==========================================================

resource "aws_security_group" "sg_ecs_financas_notificacoes" {
  name        = "ecs-financas-notificacoes-sg-${var.ambiente}"
  description = "Permite trafego para o servico de notificacoes"
  vpc_id      = aws_vpc.vpc_financas.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    security_groups = [
      # aws_security_group.sg_alb_financas_api.id,
      aws_security_group.sg_vpc_link_financas.id
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, { Name = "ecs-financas-notificacoes-sg-${var.ambiente}" })
}

# ==========================================================
# CLOUDLOGS GROUP (Grupo de Logs no CloudWatch)
# ==========================================================
resource "aws_cloudwatch_log_group" "log_group_ecs_financas_notificacoes" {
  name              = "/ecs/financas-notificacoes-${var.ambiente}"
  retention_in_days = 7
  tags              = var.common_tags
}

# ==========================================================
# TASK DEFINITION
# ==========================================================
resource "aws_ecs_task_definition" "ecs_task_definition_financas_notificacoes" {
  family                   = "ecs-financas-notificacoes-${var.ambiente}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.role_task_execution_financas_notificacoes.arn
  task_role_arn            = aws_iam_role.role_task_financas_notificacoes.arn

  container_definitions = jsonencode([
    {
      name      = "financas-notificacoes"
      image     = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.ecr_financas_notificacoes}:latest"
      essential = true

      portMappings = [{
        containerPort = 8080,
        hostPort = 8080,
        protocol = "tcp"
      }]

      environment = [
        {
          name  = "SERVER_PORT"
          value = "8080"
        }
      ]

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
          "awslogs-group"         = "/ecs/financas-notificacoes-${var.ambiente}"
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }

    }
  ])
}

# ==========================================================
# 4. ECS SERVICE (Configuração de Escalonamento e Redes)
# ==========================================================
resource "aws_ecs_service" "ecs_service_financas_notificacoes" {
  name            = "ecs-financas-notificacoes-service"
  cluster         = aws_ecs_cluster.ecs_cluster_financas.id
  task_definition = aws_ecs_task_definition.ecs_task_definition_financas_notificacoes.arn
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
    security_groups  = [aws_security_group.sg_ecs_financas_notificacoes.id]
    assign_public_ip = true
  }

  #ADICIONA REGISTRO NO ALB
  # load_balancer {
  #   target_group_arn = aws_lb_target_group.tg_financas_notificacoes.arn
  #   container_name   = "financas-notificacoes"
  #   container_port   = 8080
  # }

  #ADICIONA REGISTRO NO CLOUD MAP
  service_registries {
    registry_arn   = aws_service_discovery_service.service_discovery_financas_notificacoes.arn
    container_name = "financas-notificacoes"
    container_port = 8080
  }

  # depends_on = [aws_lb_listener_rule.routing_notificacoes]
}