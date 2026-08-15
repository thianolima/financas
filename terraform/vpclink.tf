# ==========================================================
# 1. SECURITY GROUP DO VPC LINK
# ==========================================================
resource "aws_security_group" "sg_vpc_link_financas" {
  name        = "vpc-link-financas-sg-${var.ambiente}"
  description = "Security Group para a ENI do VPC Link do API Gateway"
  vpc_id      = aws_vpc.vpc_financas.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.common_tags, { Name = "vpc-link-financas-sg-${var.ambiente}" })
}

# ==========================================================
# 2. VPC LINK (API GATEWAY HTTP V2)
# ==========================================================
# Injeta as interfaces de rede do API Gateway nas subnets da VPC
resource "aws_apigatewayv2_vpc_link" "vpclink_financas" {
  name               = "vpclink-financas-${var.ambiente}"
  security_group_ids = [aws_security_group.sg_vpc_link_financas.id]
  subnet_ids         = [
    aws_subnet.subnet_financas_publica_az_a.id,
    aws_subnet.subnet_financas_publica_az_b.id
  ]

  # tags = merge(var.common_tags, { Name = "vpclink-financas-${var.ambiente}" })
}