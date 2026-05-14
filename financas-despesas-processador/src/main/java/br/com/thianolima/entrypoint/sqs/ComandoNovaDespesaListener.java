package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarDespesaFaturaUseCase;
import br.com.thianolima.entrypoint.dto.FaturaItemDto;
import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ComandoNovaDespesaListener {

    private final ObjectMapper objectMapper;
    private final ProcessarDespesaFaturaUseCase processarDespesaFaturaUseCase;
    private final Tracer tracer;

    public ComandoNovaDespesaListener(
            ObjectMapper objectMapper,
            ProcessarDespesaFaturaUseCase processarDespesaFaturaUseCase,
            Tracer tracer
    ) {
        this.objectMapper = objectMapper;
        this.processarDespesaFaturaUseCase = processarDespesaFaturaUseCase;
        this.tracer = tracer;
    }

    @SqsListener(
            value = "${spring.cloud.aws.sqs.queue.comando-nova-despesa}",
            factory = "defaultSqsMessageListenerContainerFactory"
    )
    public void receberMensagem(
            String mensagem,
            @Header("traceId") String traceId,
            @Header("spanId") String spanId
    ){
        log.info("TraceId: {} SpanId {} mensagem: {}", traceId, spanId, mensagem);

        TraceContext context = TraceContext.newBuilder()
                .traceId(Long.parseUnsignedLong(traceId, 16))
                .spanId(Long.parseUnsignedLong(spanId, 16))
                .sampled(true)
                .build();

        Span newSpan = tracer.newChild(context).name("processar-comando-nova-despesa").start();

        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(newSpan)){
            var despesaCsv = objectMapper.readValue(mensagem, FaturaItemDto.class);
            processarDespesaFaturaUseCase.executar(despesaCsv.toDespesa());
            log.info("Sucesso Mensagem: {}", mensagem);
        } catch (Exception exception) {
            log.error("Erro: {} Mensagem: {}", exception.getMessage(), mensagem);
            throw new RuntimeException(exception);
        } finally {
            newSpan.finish();
        }
    }
}
