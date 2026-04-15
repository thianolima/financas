variable "aws_region" {
  description = "A região da AWS para provisionamento"
  type        = string
}

variable "bucket_fatura" {
  description = "Nome do bucket que ira armazenar todas faturas"
  type        = string
}

variable "sqs_comando_nova_fatura" {
  description = "Nome do sqs que ira receber a mensagem de uma nova fatura no s3"
  type        = string
}

variable "sqs_retorno_nova_fatura" {
  description = "Nome do sqs que ira receber a conclusao da fatura no s3"
  type        = string
}

variable "sqs_comando_nova_despesa" {
  description = "Nome do sqs que ira receber uma nova despesa para ser processada"
  type        = string
}

variable "common_tags" {
  description = "Um mapa de tags comuns que serão aplicadas a todos os recursos"
  type        = map(string)
}

variable "sqs_retention_seconds" {
  description = "Tempo padrão de retenção de mensagens (em segundos). Padrão: 4 dias (345600)."
  type        = number
  default     = 345600
}

variable "sqs_visibility_timeout_seconds" {
  description = "Tempo padrão para mensagem ser processado ao ser consumida (em segundos). Padrão: 30 segundos."
  type        = number
  default     = 30
}

variable "sqs_delay_seconds" {
  description = "Tempo padrão de espera para a mensagem ficar disponivel para leitura (em segundos)."
  type        = number
  default     = 0
}

variable "rds_username" {
  description = "Usuario do banco de dados"
  type        = string
  sensitive   = true
}

variable "rds_password" {
  description = "Senha do banco de dados"
  type        = string
  sensitive   = true
}

variable "ambiente" {
  description = "Ambiente em que esta sendo executa dev|hom|prd"
  type        = string
}

variable "ecr_financas_despesas_integrador" {
  description = "Nome do repositorio para modulo despesas integrador"
  type        = string
}

variable "ecr_financas_despesas_processador" {
  description = "Nome do repositorio para modulo despesas processador"
  type        = string
}

variable "ecr_financas_api" {
  description = "Nome do repositorio para modulo api"
  type        = string
}