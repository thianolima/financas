package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.provider.ProduzirComandoNovaNotificacao;
import br.com.thianolima.infrastructure.provider.sqs.dto.NotificacaoDto;
import brave.Tracer;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProduzirComandoNovaNotificacaoImpl implements ProduzirComandoNovaNotificacao {

    private final SqsTemplate sqsTemplate;
    private final String nomeFila;
    private final Tracer tracer;

    public ProduzirComandoNovaNotificacaoImpl(
            SqsTemplate sqsTemplate,
            @Value("${spring.cloud.aws.sqs.queue.comando-nova-notificacao}")
            String nomeFila,
            Tracer tracer
    ) {
        this.sqsTemplate = sqsTemplate;
        this.nomeFila = nomeFila;
        this.tracer = tracer;
    }

    @Override
    public void executar(
            Long usuarioId,
            String tipo,
            String mensagem
    ) {
        var currentSpan = tracer.currentSpan();
        var traceId =  currentSpan.context().traceIdString();
        var spanId = currentSpan.context().spanIdString();

        var notificacaoDto = new NotificacaoDto(
                usuarioId,
                tipo,
                mensagem
        );

        sqsTemplate.send(options -> options
                .queue(nomeFila)
                .header("traceId", traceId)
                .header("spanId", spanId)
                .payload(notificacaoDto)
        );
    }
}
