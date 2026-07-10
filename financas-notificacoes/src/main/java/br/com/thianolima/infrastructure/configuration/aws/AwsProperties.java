package br.com.thianolima.infrastructure.configuration.aws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwsProperties {
    private String region;
    private AwsCredentialsProperties credentials;
}