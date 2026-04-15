# Cluster ECS
resource "aws_ecs_cluster" "ecs_cluster_financas" {
  name = "ecs-cluster-financas-${var.ambiente}"
  tags = merge(var.common_tags, { Name = "ecs-cluster-financas-${var.ambiente}" })
}

# Configuração de Capacity Providers para usar Fargate e Fargate SPOT
resource "aws_ecs_cluster_capacity_providers" "ecs_cluster_providers" {
  cluster_name       = aws_ecs_cluster.ecs_cluster_financas.name
  capacity_providers = ["FARGATE", "FARGATE_SPOT"]

  default_capacity_provider_strategy {
    base              = 0
    weight            = 1
    capacity_provider = "FARGATE_SPOT" # Prioriza o menor custo (SPOT)
  }
}
