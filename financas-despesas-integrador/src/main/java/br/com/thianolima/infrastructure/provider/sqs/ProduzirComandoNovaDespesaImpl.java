package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.dto.FaturaItemDto;
import br.com.thianolima.core.provider.ProduzirComandoNovaDespesa;
import brave.Tracer;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProduzirComandoNovaDespesaImpl implements ProduzirComandoNovaDespesa {

    private final SqsTemplate sqsTemplate;
    private final String nomeFila;
    private final Tracer tracer;

    public ProduzirComandoNovaDespesaImpl(
            SqsTemplate sqsTemplate,
            @Value("${spring.cloud.aws.sqs.queue.comando-nova-despesa}")
            String nomeFila,
            Tracer tracer
    ) {
        this.sqsTemplate = sqsTemplate;
        this.nomeFila = nomeFila;
        this.tracer = tracer;
    }

    @Override
    public boolean executar(FaturaItemDto faturaItem) {
        var currentSpan = tracer.currentSpan();
        var traceId =  currentSpan.context().traceIdString();
        var spanId = currentSpan.context().spanIdString();

        log.info("TraceId: {} SpanId: {} Mensagem: {}", traceId, spanId, faturaItem);

        sqsTemplate.send(options -> options
                .queue(nomeFila)
                .payload(faturaItem)
                .header("traceId", traceId)
                .header("spanId", spanId)
                .messageGroupId(faturaItem.getFaturaId().toString())
                .messageDeduplicationId(faturaItem.getFaturaId() + "-" + faturaItem.getSequencia())
        );

        return true;
    }
}
