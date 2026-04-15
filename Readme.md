# Atualizando ECR
Segue abaixo o passo a passo de como atualizar o repositorio ECR dos modulos através do aws-cli.

### 01 - Nova Imagem
```
docker build --no-cache --build-arg MODULE_NAME=financas-despesas-integrador -t ecr-integrador .
```

### 02 - Autenticação
```
aws ecr get-login-password --region sa-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.sa-east-1.amazonaws.com
```

### 03 - Tag
```
tag financas-despesas-integrador-image:latest 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador_dev:latest
```

### 04 - Atualizando
``` 
docker push 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador_dev:latest
```