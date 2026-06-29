# Atualizando ECR
Segue abaixo o passo a passo para atualizar o ECR com uma nova imagem para os modulos através do aws-cli.

## Despesas Integrador

```
docker build --no-cache --build-arg MODULE_NAME=financas-despesas-integrador -t financas-despesas-integrador-image .
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.us-east-1.amazonaws.com
docker tag financas-despesas-integrador-image:latest 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_despesas_integrador:latest
docker push 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_despesas_integrador:latest
```

------------------------------------------------------------------------

## Despesas Processador

```
docker build --no-cache --build-arg MODULE_NAME=financas-despesas-processador -t financas-despesas-processador-image .
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.us-east-1.amazonaws.com
docker tag financas-despesas-processador-image:latest 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_despesas_processador:latest
docker push 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_despesas_processador:latest
```

------------------------------------------------------------------------

## API

```
docker build --no-cache --build-arg MODULE_NAME=financas-api -t financas-api-image .
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.us-east-1.amazonaws.com
docker tag financas-api-image:latest 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_api:latest
docker push 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_api:latest
```