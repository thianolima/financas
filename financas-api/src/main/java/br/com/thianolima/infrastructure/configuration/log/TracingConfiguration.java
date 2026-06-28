package br.com.thianolima.infrastructure.configuration.log;

import brave.Tracing;
import brave.TracingCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfiguration {

    @Bean
    public TracingCustomizer traceIdCustomizer() {
        return new TracingCustomizer() {
            @Override
            public void customize(Tracing.Builder builder) {
                builder.traceId128Bit(false);
            }
        };
    }
}
