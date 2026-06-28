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
ENV AWS_S3_BUCKET_EXTRATO=thianolima-financas-extratos-dev
ENV AWS_S3_BUCKET_FATURA=thianolima-financas-faturas-dev
ENV AWS_SQS_COMANDO_NOVA_FATURA=sqs-comando-nova-fatura-dev
ENV AWS_SQS_RETORNO_NOVA_FATURA=sqs-retorno-nova-fatura-dev
ENV AWS_SQS_COMANDO_NOVA_DESPESA=sqs-comando-nova-despesa-dev.fifo
ENV AWS_SQS_COMANDO_PROCESSAR_REGRAS=sqs-comando-processar-regras-dev.fifo
ENV AWS_SQS_COMANDO_PROCESSAR_REGRAS_DLQ=sqs-comando-processar-regras-dev-dlq
ENV AWS_SQS_RETORNO_PROCESSAR_REGRAS=sqs-retorno-processar-regras-dev

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
