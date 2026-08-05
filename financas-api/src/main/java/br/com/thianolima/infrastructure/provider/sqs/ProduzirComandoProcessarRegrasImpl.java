package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.model.ComandoProcessarRegras;
import br.com.thianolima.core.provider.message.ProduzirComandoProcessarRegras;
import br.com.thianolima.infrastructure.provider.sqs.dto.ComandoProcessarRegraDto;
import brave.Tracer;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProduzirComandoProcessarRegrasImpl implements ProduzirComandoProcessarRegras {

    private final SqsTemplate sqsTemplate;
    private final String nomeFila;
    private final Tracer tracer;

    public ProduzirComandoProcessarRegrasImpl(
            SqsTemplate sqsTemplate,
            @Value("${spring.cloud.aws.sqs.queue.comando-processar-regras}")
            String nomeFila,
            Tracer tracer
    ) {
        this.sqsTemplate = sqsTemplate;
        this.nomeFila = nomeFila;
        this.tracer = tracer;
    }

    @Override
    public void executar(ComandoProcessarRegras comandoProcessarRegras) {
        var currentSpan = tracer.currentSpan();
        var traceId =  currentSpan.context().traceIdString();
        var spanId = currentSpan.context().spanIdString();
        var comandoProcessarRegraDto = new ComandoProcessarRegraDto(comandoProcessarRegras);

        log.info("INICIO - Envio Comando Processar Regras TraceId: {} SpanId: {} Mensagem: {} Fila: {}",
                traceId, spanId, comandoProcessarRegraDto, nomeFila);

        sqsTemplate.send(options -> options
                .queue(nomeFila)
                .payload(comandoProcessarRegraDto)
                .header("traceId", traceId)
                .header("spanId", spanId)
                .messageGroupId(traceId)
                .messageDeduplicationId(UUID.randomUUID().toString())
        );

        log.info("FIM - Envio Comando Processar Regras TraceId: {} SpanId: {} Mensagem: {} Fila: {}",
                traceId, spanId, comandoProcessarRegraDto, nomeFila);
    }
}
