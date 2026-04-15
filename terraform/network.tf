# 1. VPC Principal
resource "aws_vpc" "vpc_financas" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = merge(var.common_tags, { Name = "vpc-financas-${var.ambiente}" })
}

# 2. Internet Gateway
resource "aws_internet_gateway" "igw_financas" {
  vpc_id = aws_vpc.vpc_financas.id
  tags   = merge(var.common_tags, { Name = "igw-financas" })
}

# ---------------------------------------------------------
# BLOCO 1: Subnets principais (Padrão)
# ---------------------------------------------------------
resource "aws_subnet" "subnet_publica" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-publica" })
}

resource "aws_subnet" "subnet_privada" {
  vpc_id            = aws_vpc.vpc_financas.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "${var.aws_region}a"
  tags = merge(var.common_tags, { Name = "subnet-privada" })
}

# ---------------------------------------------------------
# BLOCO 2: Subnets específicas (Finanças)
# ---------------------------------------------------------
resource "aws_subnet" "subnet_financas_publica" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-financas-publica" })
}

resource "aws_subnet" "subnet_financas_privada" {
  vpc_id            = aws_vpc.vpc_financas.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "${var.aws_region}a"
  tags = merge(var.common_tags, { Name = "subnet-financas-privada" })
}

# ---------------------------------------------------------
# TABELAS DE ROTAS
# ---------------------------------------------------------

# Tabela Pública Geral
resource "aws_route_table" "route_table_publica" {
  vpc_id = aws_vpc.vpc_financas.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_financas.id
  }
  tags = merge(var.common_tags, { Name = "route-table-publica" })
}

# Tabela Pública Específica (Finanças)
resource "aws_route_table" "route_table_financas_publica" {
  vpc_id = aws_vpc.vpc_financas.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_financas.id
  }
  tags = merge(var.common_tags, { Name = "route-table-financas-publica" })
}

# Tabelas Privadas (Apenas locais, sem rota de saída direta)
resource "aws_route_table" "route_table_privada" {
  vpc_id = aws_vpc.vpc_financas.id
  tags = merge(var.common_tags, { Name = "route-table-privada" })
}

resource "aws_route_table" "route_table_financas_privada" {
  vpc_id = aws_vpc.vpc_financas.id
  tags = merge(var.common_tags, { Name = "route-financas-table-privada" })
}

# ---------------------------------------------------------
# ASSOCIAÇÕES
# ---------------------------------------------------------

# Subnets Públicas -> Apontam para as respectivas tabelas públicas com IGW
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.subnet_publica.id
  route_table_id = aws_route_table.route_table_publica.id
}

resource "aws_route_table_association" "assoc_financas_publica" {
  subnet_id      = aws_subnet.subnet_financas_publica.id
  route_table_id = aws_route_table.route_table_financas_publica.id
}

# Subnets Privadas -> Mantidas nas tabelas privadas ou conforme sua necessidade
resource "aws_route_table_association" "privada_assoc" {
  subnet_id      = aws_subnet.subnet_privada.id
  route_table_id = aws_route_table.route_table_privada.id
}

resource "aws_route_table_association" "assoc_financas_privada" {
  subnet_id      = aws_subnet.subnet_financas_privada.id
  route_table_id = aws_route_table.route_table_financas_privada.id
}