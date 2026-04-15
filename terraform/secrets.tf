# 1. Recurso do Segredo para a Senha do Banco de Dados
resource "aws_secretsmanager_secret" "rds_password" {
  name        = "financas/rds/password-${var.ambiente}"
  description = "Senha do banco de dados RDS para o sistema de financas"

  tags = merge(var.common_tags, { Name = "rds-password-${var.ambiente}" })
}