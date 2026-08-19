# ==========================================================
# 1. CERTIFICADO SSL/TLS GRATUITO (ACM)
# ==========================================================
resource "aws_acm_certificate" "acm_financas_api" {
  domain_name       = "api.thianolima.com"
  validation_method = "DNS"

  tags = merge(var.common_tags, { Name = "acm-api" })

  lifecycle {
    create_before_destroy = true
  }
}

# ==========================================================
# 2. REGISTROS CNAME NO ROUTE 53 PARA VALIDAÇÃO AUTOMÁTICA
# ==========================================================
resource "aws_route53_record" "cert_validation_record" {
  for_each = {
    for dvo in aws_acm_certificate.acm_financas_api.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }

  allow_overwrite = true
  name            = each.value.name
  records         = [each.value.record]
  ttl             = 60
  type            = each.value.type
  zone_id         = data.aws_route53_zone.principal.zone_id
}

# ==========================================================
# 3. VALIDAÇÃO DO CERTIFICADO
# ==========================================================
resource "aws_acm_certificate_validation" "cert_validation" {
  certificate_arn         = aws_acm_certificate.acm_financas_api.arn
  validation_record_fqdns = [for record in aws_route53_record.cert_validation_record : record.fqdn]
}

# Output do ARN do Certificado Validado
output "acm_certificate_arn" {
  description = "ARN do certificado SSL validado no ACM"
  value       = aws_acm_certificate_validation.cert_validation.certificate_arn
}