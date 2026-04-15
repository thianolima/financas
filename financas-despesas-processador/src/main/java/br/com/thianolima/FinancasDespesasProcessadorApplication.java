
package br.com.thianolima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class FinancasDespesasProcessadorApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancasDespesasProcessadorApplication.class, args);
    }

    @Bean
    public CommandLineRunner apresentarParametros(
            @Value("${spring.cloud.aws.credentials.access-key}")String accessKey,
            @Value("${spring.cloud.aws.credentials.secret-key}")String secretKey,
            @Value("${spring.cloud.aws.region}")String region
    ) {
        return args -> {
            log.info("DEBUG AWS SECRET-KEY CARREGADA: {}", secretKey);
            log.info("DEBUG AWS ACCESS-KEY CARREGADA: {}", accessKey);
            log.info("DEBUG AWS REGION CARREGADA: {}", region);
            log.info("Financas Despesas Processador");
        };
    }

}