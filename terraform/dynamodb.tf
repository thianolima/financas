resource "aws_dynamodb_table" "notificacoes" {
  name         = "notificacoes"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "notificacao_id" # Partition Key
  range_key    = "data_hora_criacao"   # Sort Key

  # Definição dos atributos usados como chaves
  attribute {
    name = "notificacao_id"
    type = "S" # String (ex: UUID)
  }

  attribute {
    name = "data_hora_criacao"
    type = "S" # String (ex: ISO-8601 "2026-07-02T14:19:52Z")
  }

  # Configuração do TTL
  ttl {
    attribute_name = "data_expurgo"
    enabled        = true
  }

  # Habilita a criptografia em repouso gerenciada pela AWS (padrão KMS)
  server_side_encryption {
    enabled = true
  }

  tags = merge(var.common_tags, {
    Name = "notificacoes"
  })
}
