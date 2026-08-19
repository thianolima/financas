# INFRAESTRUTURA MIGRADA PARA API GATEWAY HTTP (V2)

# ==========================================================
# 1. SECURITY GROUP DO LOAD BALANCER
# ==========================================================
# resource "aws_security_group" "sg_alb_financas_api" {
#   name        = "alb-financas-api-sg-${var.ambiente}"
#   description = "Permite acesso externo via HTTP para o Load Balancer"
#   vpc_id      = aws_vpc.vpc_financas.id
#
#   # Entrada: Qualquer IP da internet pode acessar o ALB na porta 80
#   ingress {
#     from_port   = 80
#     to_port     = 80
#     protocol    = "tcp"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
#
#   # Saída: O ALB pode se comunicar com qualquer destino (essencial para health checks)
#   egress {
#     from_port   = 0
#     to_port     = 0
#     protocol    = "-1"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
#
#   tags = merge(var.common_tags, { Name = "alb-financas-api-sg-${var.ambiente}" })
# }

# ==========================================================
# 2. APPLICATION LOAD BALANCER
# ==========================================================
# resource "aws_lb" "alb_financas_api" {
#   name               = "alb-financas-api-${var.ambiente}"
#   internal           = false
#   load_balancer_type = "application"
#   security_groups    = [aws_security_group.sg_alb_financas_api.id]
#
#   subnets            = [
#     aws_subnet.subnet_financas_publica_az_a.id,
#     aws_subnet.subnet_financas_publica_az_b.id
#   ]
#
#   enable_deletion_protection = false
#
#   tags = merge(var.common_tags, { Name = "alb-financas-api-${var.ambiente}" })
# }

# ==========================================================
# 3. TARGET GROUP DA API PRINCIPAL
# ==========================================================
# resource "aws_lb_target_group" "tg_financas_api" {
#   name        = "tg-financas-api-${var.ambiente}"
#   port        = 8080
#   protocol    = "HTTP"
#   vpc_id      = aws_vpc.vpc_financas.id
#   target_type = "ip" # Obrigatório para o modo Fargate
#
#   health_check {
#     path                = "/actuator/health"
#     interval            = 30
#     timeout             = 15
#     healthy_threshold   = 2
#     unhealthy_threshold = 5
#     matcher             = "200"
#   }
#
#   tags = merge(var.common_tags, { Name = "tg-financas-api-${var.ambiente}" })
# }

# ==========================================================
# 4. LISTENER ALB
# ==========================================================
# resource "aws_lb_listener" "api_listener_http" {
#   load_balancer_arn = aws_lb.alb_financas_api.arn
#   port              = "80"
#   protocol          = "HTTP"
#
#   # Ação padrão: Tudo que não casar com regras específicas cai na API principal
#   default_action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.tg_financas_api.arn
#   }
# }

# ==========================================================
# 5. TARGET GROUP PARA NOTIFICAÇÕES (SSE)
# ==========================================================
# resource "aws_lb_target_group" "tg_financas_notificacoes" {
#   name        = "tg-financas-notificacoes-${var.ambiente}"
#   port        = 8080
#   protocol    = "HTTP"
#   vpc_id      = aws_vpc.vpc_financas.id
#   target_type = "ip" # Obrigatório para o modo Fargate
#
#   health_check {
#     path                = "/actuator/health"
#     interval            = 30
#     timeout             = 15
#     healthy_threshold   = 2
#     unhealthy_threshold = 5
#     matcher             = "200"
#   }
#
#   tags = merge(var.common_tags, { Name = "tg-financas-notificacoes-${var.ambiente}" })
# }

# ==========================================================
# 6. REGRA DE ROTEAMENTO PARA AS NOTIFICAÇÕES (/notificacoes/*)
# ==========================================================
# resource "aws_lb_listener_rule" "routing_notificacoes" {
#   listener_arn = aws_lb_listener.api_listener_http.arn
#   priority     = 10 # Prioridade baixa avaliada antes da "default action"
#
#   action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.tg_financas_notificacoes.arn
#   }
#
#   # Condição de Caminho (Path)
#   condition {
#     path_pattern {
#       values = ["/notificacoes","/notificacoes/*"]
#     }
#   }
# }