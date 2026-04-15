# Estágio 1: Build (Igual para todos)
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Runtime (Aqui definimos qual serviço sobe)
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Usamos um ARG para decidir qual JAR copiar
ARG MODULE_NAME=financas-api
COPY --from=build /app/${MODULE_NAME}/target/*.jar app.jar


ENV AWS_RDS_URL="jdbc:mysql://rds-financas.cjkk4wk6acu9.sa-east-1.rds.amazonaws.com:3306/financasdb?useSSL=false&allowPublicKeyRetrieval=true"
ENV AWS_RDS_USER=admin
ENV AWS_REGION=sa-east-1
ENV AWS_S3_BUCKET_EXTRATO=thianolima-financas-extratos-dev
ENV AWS_S3_BUCKET_FATURA=thianolima-financas-faturas-dev
ENV SQS_COMANDO_NOVA_FATURA=sqs-comando-nova-fatura-dev.fifo
ENV SQS_RETORNO_NOVA_FATURA=sqs-retorno-nova-fatura-dev
ENV SQS_COMANDO_NOVA_DESPESA=sqs-comando-nova-despesa-dev

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# 01 -> GERA NOVA IMAGEM
# docker build --no-cache --build-arg MODULE_NAME=financas-despesas-integrador -t ecr-integrador .

# 02 -> AUTENTICA O DOCKER NA AWS
# aws ecr get-login-password --region sa-east-1 | docker login --username AWS --password-stdin 841816327169.dkr.ecr.sa-east-1.amazonaws.com

# 03 -> GERANDO NOVA TAG PARA O REPOSITORIO
# tag financas-despesas-integrador-image:latest 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador_dev:latest

# 04 -> ENVIANDO NOVA IMAGEM PARA O ECR
# docker push 841816327169.dkr.ecr.sa-east-1.amazonaws.com/ecr_financas_despesas_integrador_dev:latest