# Intellij

## Enviroments
Todas as enviroments utilizadas no Intellij para os modulos do projeto.
Os valores abaixo são apenas para desenvolvimento local, favor conferilos antes de subir em produção.

```
AWS_CREDENTIALS_ACCESS_KEY=fake;
AWS_CREDENTIALS_SECRET_KEY=fake;
AWS_RDS_PASSWORD=admin;
AWS_RDS_URL="jdbc:localhost:3306/financasdb?useSSL=true&allowPublicKeyRetrieval=true";
AWS_RDS_USER=admin;
AWS_REGION=us-east-1;
AWS_S3_BUCKET_FATURA=financas-faturas-dev;
AWS_SQS_RETORNO_NOVA_FATURA=sqs-retorno-nova-fatura-dev;
AWS_SQS_COMANDO_NOVA_DESPESA=sqs-comando-nova-despesa-dev.fifo;
AWS_SQS_COMANDO_PROCESSAR_REGRAS=sqs-comando-processar-regras-dev.fifo;
AWS_SQS_COMANDO_NOVA_NOTIFICACAO=sqs-comando-nova-notificacao-dev;
```

# ECR
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

------------------------------------------------------------------------

## Notificacoes

```
docker build --no-cache --build-arg MODULE_NAME=financas-notificacoes -t financas-notificacoes-image .
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.us-east-1.amazonaws.com
docker tag financas-notificacoes-image:latest 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_notificacoes:latest
docker push 841816327169.dkr.ecr.us-east-1.amazonaws.com/ecr_financas_notificacoes:latest
```
------------------------------------------------------------------------


