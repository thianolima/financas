# 1. Busca a Zona Hospedada
data "aws_route53_zone" "principal" {
  name         = "thianolima.com."
  private_zone = false
}

# 2. Cria o subdomínio da sua API
resource "aws_route53_record" "api_dns" {
  zone_id = data.aws_route53_zone.principal.zone_id
  name    = "api.thianolima.com"
  type    = "A"

  alias {
    name                   = aws_lb.alb_financas_api.dns_name
    zone_id                = aws_lb.alb_financas_api.zone_id
    evaluate_target_health = true
  }
}