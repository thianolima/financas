# 1. Busca a Zona Hospedada Pública
data "aws_route53_zone" "principal" {
  name         = "thianolima.com."
  private_zone = false
}

# 2. Aponta api.thianolima.com para o API Gateway HTTP (V2)
resource "aws_route53_record" "api_dns" {
  zone_id = data.aws_route53_zone.principal.zone_id
  name    = "api.thianolima.com"
  type    = "A"

  alias {
    name                   = aws_apigatewayv2_domain_name.api_domain.domain_name_configuration[0].target_domain_name
    zone_id                = aws_apigatewayv2_domain_name.api_domain.domain_name_configuration[0].hosted_zone_id
    evaluate_target_health = false
  }
}