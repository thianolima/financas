# =========================================================
# 1. VPC PRINCIPAL
# =========================================================
resource "aws_vpc" "vpc_financas" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = merge(var.common_tags, { Name = "vpc-financas-${var.ambiente}" })
}

# =========================================================
# 2. INTERNET GATEWAY
# =========================================================
resource "aws_internet_gateway" "igw_financas" {
  vpc_id = aws_vpc.vpc_financas.id
  tags   = merge(var.common_tags, { Name = "igw-financas" })
}

# =========================================================
# 3. SUBNETS PÚBLICAS (4 no total para transição)
# =========================================================

# --- SUBNETS ANTIGAS (AZ-A) ---
resource "aws_subnet" "subnet_publica" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-publica" })
}

resource "aws_subnet" "subnet_financas_publica" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-financas-publica" })
}

# --- SUBNETS NOVAS (REFACTORING) ---
resource "aws_subnet" "subnet_financas_publica_az_a" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.10.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-financas-publica-az-a" })
}

resource "aws_subnet" "subnet_financas_publica_az_b" {
  vpc_id                  = aws_vpc.vpc_financas.id
  cidr_block              = "10.0.11.0/24"
  availability_zone       = "${var.aws_region}b"
  map_public_ip_on_launch = true
  tags = merge(var.common_tags, { Name = "subnet-financas-publica-az-b" })
}

# =========================================================
# 4. SUBNETS PRIVADAS
# =========================================================
resource "aws_subnet" "subnet_privada" {
  vpc_id            = aws_vpc.vpc_financas.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "${var.aws_region}b"
  tags = merge(var.common_tags, { Name = "subnet-privada" })
}

resource "aws_subnet" "subnet_financas_privada" {
  vpc_id            = aws_vpc.vpc_financas.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "${var.aws_region}a"
  tags = merge(var.common_tags, { Name = "subnet-financas-privada" })
}

# =========================================================
# 5. TABELAS DE ROTAS
# =========================================================
resource "aws_route_table" "route_table_publica" {
  vpc_id = aws_vpc.vpc_financas.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_financas.id
  }
  tags = merge(var.common_tags, { Name = "route-table-publica" })
}

resource "aws_route_table" "route_table_financas_publica" {
  vpc_id = aws_vpc.vpc_financas.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_financas.id
  }
  tags = merge(var.common_tags, { Name = "route-table-financas-publica" })
}

# Tabelas Privadas
resource "aws_route_table" "route_table_privada" {
  vpc_id = aws_vpc.vpc_financas.id
  tags = merge(var.common_tags, { Name = "route-table-privada" })
}

resource "aws_route_table" "route_table_financas_privada" {
  vpc_id = aws_vpc.vpc_financas.id
  tags = merge(var.common_tags, { Name = "route-table-financas-privada" })
}

# =========================================================
# 6. ASSOCIAÇÕES DAS TABELAS DE ROTAS
# =========================================================

# --- Associações das Subnets Públicas Antigas ---
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.subnet_publica.id
  route_table_id = aws_route_table.route_table_publica.id
}

resource "aws_route_table_association" "assoc_financas_publica" {
  subnet_id      = aws_subnet.subnet_financas_publica.id
  route_table_id = aws_route_table.route_table_publica.id
}

# --- Associações das Subnets Públicas Novas ---
resource "aws_route_table_association" "public_assoc_az_a" {
  subnet_id      = aws_subnet.subnet_financas_publica_az_a.id
  route_table_id = aws_route_table.route_table_financas_publica.id
}

resource "aws_route_table_association" "assoc_financas_publica_az_b" {
  subnet_id      = aws_subnet.subnet_financas_publica_az_b.id
  route_table_id = aws_route_table.route_table_financas_publica.id
}

# --- Associações das Subnets Privadas ---
resource "aws_route_table_association" "priv_assoc_1" {
  subnet_id      = aws_subnet.subnet_privada.id
  route_table_id = aws_route_table.route_table_privada.id
}

resource "aws_route_table_association" "priv_assoc_2" {
  subnet_id      = aws_subnet.subnet_financas_privada.id
  route_table_id = aws_route_table.route_table_financas_privada.id
}