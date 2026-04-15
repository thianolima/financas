# Security Group para o RDS
resource "aws_security_group" "sg_rds" {
  name        = "public-rds-sg"
  description = "Liberar acesso ao RDS para conexao externa"

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = var.common_tags
}

# Instância do RDS
resource "aws_db_instance" "rds_financas" {
  identifier           = "rds-financas"
  allocated_storage    = 20
  storage_type         = "gp2"
  instance_class       = "db.t4g.micro"
  engine               = "mysql"
  engine_version       = "8.4.7"
  parameter_group_name = "default.mysql8.4"
  db_name              = "financasdb"
  username             = var.rds_username
  password             = var.rds_password
  publicly_accessible  = true # Essencial para o DBeaver funcionar
  skip_final_snapshot  = true # Não cria backup pago ao destruir o banco com terraform destroy
  vpc_security_group_ids = [aws_security_group.sg_rds.id]
  tags = var.common_tags
}

output "rds_endpoint" {
  value = aws_db_instance.rds_financas.endpoint
}



