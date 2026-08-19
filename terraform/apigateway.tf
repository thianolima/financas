# ==========================================================
# 1. API GATEWAY HTTP (V2)
# ==========================================================
resource "aws_apigatewayv2_api" "apigtw" {
  name          = "api-financas-${var.ambiente}"
  protocol_type = "HTTP"

  cors_configuration {
    allow_origins = ["*"]
    allow_methods = ["GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"]
    allow_headers = ["*"]
    max_age       = 300
  }
}

# ==========================================================
# 2. CLOUDWATCH LOG GROUP & STAGE $DEFAULT
# ==========================================================
resource "aws_cloudwatch_log_group" "apigtw_logs" {
  name              = "/apigateway/api-financas-${var.ambiente}"
  retention_in_days = 3
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.apigtw.id
  name        = "$default"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.apigtw_logs.arn
    format = jsonencode({
      requestId        = "$context.requestId"
      ip               = "$context.identity.sourceIp"
      requestTime      = "$context.requestTime"
      httpMethod       = "$context.httpMethod"
      routeKey         = "$context.routeKey"
      status           = "$context.status"
      protocol         = "$context.protocol"
      responseLength   = "$context.responseLength"
      error            = "$context.error.message"
      integrationError = "$context.integrationErrorMessage"
    })
  }
}

# ==========================================================
# 3. INTEGRAÇÕES (VPC LINK + CLOUD MAP)
# ==========================================================

# A. Integração para a API Principal
resource "aws_apigatewayv2_integration" "integration_api" {
  api_id             = aws_apigatewayv2_api.apigtw.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  connection_type    = "VPC_LINK"
  connection_id      = aws_apigatewayv2_vpc_link.vpclink_financas.id

  integration_uri        = aws_service_discovery_service.service_discovery_financas_api.arn
  payload_format_version = "1.0"
}

# B. Integração para o Serviço de Notificações
resource "aws_apigatewayv2_integration" "integration_notificacoes" {
  api_id             = aws_apigatewayv2_api.apigtw.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  connection_type    = "VPC_LINK"
  connection_id      = aws_apigatewayv2_vpc_link.vpclink_financas.id

  integration_uri        = aws_service_discovery_service.service_discovery_financas_notificacoes.arn
  payload_format_version = "1.0"
}

# ==========================================================
# 4. ROTAS (ROUTES)
# ==========================================================

# Rota de Diagnóstico: Actuator / Health Check
resource "aws_apigatewayv2_route" "route_actuator" {
  api_id    = aws_apigatewayv2_api.apigtw.id
  route_key = "GET /actuator/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.integration_api.id}"
}

# Rota para o caminho exato: GET/POST /notificacoes
resource "aws_apigatewayv2_route" "route_notificacoes_exact" {
  api_id    = aws_apigatewayv2_api.apigtw.id
  route_key = "ANY /notificacoes"
  target    = "integrations/${aws_apigatewayv2_integration.integration_notificacoes.id}"
}

# Rota para os subcaminhos: /notificacoes/stream, etc.
resource "aws_apigatewayv2_route" "route_notificacoes" {
  api_id    = aws_apigatewayv2_api.apigtw.id
  route_key = "ANY /notificacoes/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.integration_notificacoes.id}"
}

# Rota Catch-all: ANY /{proxy+} -> API Principal
resource "aws_apigatewayv2_route" "route_default" {
  api_id    = aws_apigatewayv2_api.apigtw.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.integration_api.id}"
}

# ==========================================================
# 5. DOMÍNIO CUSTOMIZADO (api.thianolima.com)
# ==========================================================

# Registra o subdomínio no API Gateway utilizando o certificado criado no ACM
resource "aws_apigatewayv2_domain_name" "api_domain" {
  domain_name = "api.thianolima.com"

  domain_name_configuration {
    certificate_arn = aws_acm_certificate_validation.cert_validation.certificate_arn
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"
  }
}

# Mapeia o subdomínio diretamente para o Stage $default
resource "aws_apigatewayv2_api_mapping" "api_mapping" {
  api_id      = aws_apigatewayv2_api.apigtw.id
  domain_name = aws_apigatewayv2_domain_name.api_domain.id
  stage       = aws_apigatewayv2_stage.default.id
}

# ==========================================================
# 6. OUTPUTS
# ==========================================================
output "apigateway_endpoint" {
  description = "URL nativa pública gerada pela AWS para testes"
  value       = aws_apigatewayv2_stage.default.invoke_url
}

output "apigateway_target_domain_name" {
  description = "Target domain gerado pela AWS para uso no Route 53"
  value       = aws_apigatewayv2_domain_name.api_domain.domain_name_configuration[0].target_domain_name
}