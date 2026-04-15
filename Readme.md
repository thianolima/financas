# Atualizando ECR
Segue abaixo o passo a passo para atualizar o ECR com uma nova imagem para os modulos através do aws-cli.

## Despesas Integrador
### 01 - Nova Imagem
```
docker build --no-cache --build-arg MODULE_NAME=financas-despesas-integrador -t financas-despesas-integrador-image .
```

### 02 - Autenticação
```
aws ecr get-login-password --region sa-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.sa-east-1.amazonaws.com
```

### 03 - Tag
```
docker tag financas-despesas-integrador-image:latest 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador:latest
```

### 04 - Atualizando
``` 
docker push 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador:latest
```

------------------------------------------------------------------------

## Despesas Processador

### 01 - Nova Imagem
```
docker build --no-cache --build-arg MODULE_NAME=financas-despesas-processador -t financas-despesas-processador-image .
```

### 02 - Autenticação
```
aws ecr get-login-password --region sa-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.sa-east-1.amazonaws.com
```

### 03 - Tag
```
docker tag financas-despesas-processador-image:latest 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_processador:latest
```

### 04 - Atualizando
``` 
docker push 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_processador:latest
```