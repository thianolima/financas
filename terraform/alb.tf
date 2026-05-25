# ==========================================================
# 1. SECURITY GROUP DO LOAD BALANCER
# ==========================================================
resource "aws_security_group" "sg_alb_financas_api" {
  name        = "alb-financas-api-sg-${var.ambiente}"
  description = "Permite acesso externo via HTTP para o Load Balancer"
  vpc_id      = aws_vpc.vpc_financas.id

  # Entrada: Qualquer IP da internet pode acessar o ALB na porta 80
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Saída: O ALB pode se comunicar com qualquer destino (essencial para health checks)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, { Name = "alb-financas-api-sg-${var.ambiente}" })
}

# ==========================================================
# 2. APPLICATION LOAD BALANCER (O Garçom)
# ==========================================================
resource "aws_lb" "alb_financas_api" {
  name               = "alb-financas-api-${var.ambiente}"
  internal           = false # "false" significa exposto para a internet
  load_balancer_type = "application"
  security_groups    = [aws_security_group.sg_alb_financas_api.id]

  # O ALB obrigatoriamente precisa estar nas subnets PÚBLICAS
  subnets            = [
    aws_subnet.subnet_financas_publica_az_a.id,
    aws_subnet.subnet_financas_publica_az_b.id
  ]

  tags = merge(var.common_tags, { Name = "alb-financas-api-${var.ambiente}" })
}

# ==========================================================
# 3. TARGET GROUP (O Destino Interno)
# ==========================================================
resource "aws_lb_target_group" "tg_financas_api" {
  name        = "tg-financas-api-${var.ambiente}"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.vpc_financas.id
  target_type = "ip" # Obrigatório para o modo Fargate

  health_check {
    path                = "/actuator/health"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
    matcher             = "200"
  }

  tags = merge(var.common_tags, { Name = "tg-finacnas-api-${var.ambiente}" })
}

# ==========================================================
# 4. LISTENER ALB
# ==========================================================
resource "aws_lb_listener" "api_listener_http" {
  load_balancer_arn = aws_lb.alb_financas_api.arn
  port              = "80"
  protocol          = "HTTP"

  # Tudo que chegar na porta 80 do ALB é encaminhado para os containers do Target Group
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg_financas_api.arn
  }
}

# ==========================================================
# OUTPUT
# ==========================================================
output "api_url_acesso" {
  value       = aws_lb.alb_financas_api.dns_name
  description = "Acesse sua API remotamente através deste endereço"
}
