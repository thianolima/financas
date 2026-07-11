resource "aws_sqs_queue" "sqs-comando-nova-fatura" {
  name = var.sqs_comando_nova_fatura
  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags 
}

resource "aws_sqs_queue" "sqs-comando-nova-despesa" {
  name = "${var.sqs_comando_nova_despesa}.fifo"
  fifo_queue = true
  content_based_deduplication = true
  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags
}

resource "aws_sqs_queue" "sqs-retorno-nova-fatura" {
  name = var.sqs_retorno_nova_fatura
  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags
}

resource "aws_sqs_queue" "sqs-comando-processar-regras" {
  name = "${var.sqs_comando_processar_regras}.fifo"
  fifo_queue = true

  content_based_deduplication = false
  deduplication_scope = "messageGroup"
  fifo_throughput_limit = "perMessageGroupId"

  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.sqs-comando-processar-regras-dlq.arn
    maxReceiveCount     = 5
  })
}

resource "aws_sqs_queue" "sqs-comando-processar-regras-dlq" {
  name =  "${var.sqs_comando_processar_regras_dlq}.fifo"
  fifo_queue = true

  content_based_deduplication = false
  deduplication_scope = "messageGroup"
  fifo_throughput_limit = "perMessageGroupId"

  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags
}

resource "aws_sqs_queue" "sqs-comando-nova-notificacao" {
  name = var.sqs_comando_nova_notificacao
  delay_seconds = var.sqs_delay_seconds
  visibility_timeout_seconds = var.sqs_visibility_timeout_seconds
  message_retention_seconds = var.sqs_retention_seconds
  tags  = var.common_tags
}