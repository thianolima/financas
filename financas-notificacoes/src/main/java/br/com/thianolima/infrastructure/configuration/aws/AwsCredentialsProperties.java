package br.com.thianolima.infrastructure.configuration.aws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwsCredentialsProperties {
    private String accessKey;
    private String secretKey;
}