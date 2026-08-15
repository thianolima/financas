# ==========================================================
# 1. NAMESPACE PRIVADO DO CLOUD MAP
# ==========================================================
# Cria um domínio DNS interno acessível apenas dentro da VPC
resource "aws_service_discovery_private_dns_namespace" "cloudmap_financas" {
  name        = "cloudmap.financas"
  description = "Namespace DNS privado para Discovery dos serviços no ECS"
  vpc         = aws_vpc.vpc_financas.id

  tags = merge(var.common_tags, { Name = "cloudmap-financas-${var.ambiente}" })
}

# ==========================================================
# 2. SERVIÇO DISCOVERY - API
# ==========================================================
# Registra as instâncias (tasks) da API principal
resource "aws_service_discovery_service" "service_discovery_financas_api" {
  name = "ecs_api"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.cloudmap_financas.id

    dns_records {
      ttl  = 10
      type = "SRV"
    }

    routing_policy = "MULTIVALUE"
  }

  # Health check gerenciado pelo ECS/Cloud Map
  health_check_custom_config {
    failure_threshold = 1
  }

  tags = merge(var.common_tags, { Name = "service-discovery-financas-api-${var.ambiente}" })
}

# ==========================================================
# 2. SERVIÇO DISCOVERY - NOTIFICACAO
# ==========================================================
# Registra as instâncias (tasks) da API principal
resource "aws_service_discovery_service" "service_discovery_financas_notificacoes" {
  name = "ecs_notificacoes"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.cloudmap_financas.id

    dns_records {
      ttl  = 10
      type = "SRV"
    }

    routing_policy = "MULTIVALUE"
  }

  # Health check gerenciado pelo ECS/Cloud Map
  health_check_custom_config {
    failure_threshold = 1
  }

  tags = merge(var.common_tags, { Name = "service-discovery-financas-notificacoes-${var.ambiente}" })
}