# 1. Criação do Bucket S3
resource "aws_s3_bucket" "s3_financeiro" {
  bucket = var.bucket_fatura
  tags   = var.common_tags 
}

# 2. Controle de Propriedade (Referenciando s3_financeiro)
resource "aws_s3_bucket_ownership_controls" "s3_financeiro_controls" {
  bucket = aws_s3_bucket.s3_financeiro.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

# 3. ACL Privada (Referenciando s3_financeiro e os controles acima)
resource "aws_s3_bucket_acl" "s3_acl" {
  depends_on = [aws_s3_bucket_ownership_controls.s3_financeiro_controls]
  bucket     = aws_s3_bucket.s3_financeiro.id
  acl        = "private"
}

# 4. Permissão para o S3 postar na fila SQS
resource "aws_sqs_queue_policy" "s3_notificacao_policy" {
  queue_url = aws_sqs_queue.sqs-comando-nova-fatura.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = { Service = "s3.amazonaws.com" }
        Action    = "sqs:SendMessage"
        Resource  = aws_sqs_queue.sqs-comando-nova-fatura.arn
        Condition = {
          ArnLike = { "aws:SourceArn" = aws_s3_bucket.s3_financeiro.arn }
        }
      }
    ]
  })
}

# 5. Configuração do Evento com filtro de pasta
resource "aws_s3_bucket_notification" "s3_comando_nova_fatura" {
  bucket = aws_s3_bucket.s3_financeiro.id
  queue {
    id            = "notificacao-fatura-csv"
    queue_arn     = aws_sqs_queue.sqs-comando-nova-fatura.arn
    events        = ["s3:ObjectCreated:*"]
    # Filtro: Apenas arquivos .csv
    filter_suffix = ".csv"
  }
  depends_on = [aws_sqs_queue_policy.s3_notificacao_policy]
}