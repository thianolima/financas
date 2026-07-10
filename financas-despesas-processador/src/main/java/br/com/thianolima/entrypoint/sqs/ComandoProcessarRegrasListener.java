package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarRegrasEmLoteUseCase;
import br.com.thianolima.entrypoint.dto.ComandoProcessarRegraDto;
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
public class ComandoProcessarRegrasListener {

    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final ProcessarRegrasEmLoteUseCase processarRegrasEmLoteUseCase;

    public ComandoProcessarRegrasListener(
            ObjectMapper objectMapper,
            Tracer tracer,
            ProcessarRegrasEmLoteUseCase processarRegrasEmLoteUseCase
    ) {
        this.objectMapper = objectMapper;
        this.tracer = tracer;
        this.processarRegrasEmLoteUseCase = processarRegrasEmLoteUseCase;
    }

    @SqsListener(
            value = "${spring.cloud.aws.sqs.queue.comando-processar-regras}",
            factory = "defaultSqsMessageListenerContainerFactory"
    )
    public void receberMensagem(
            String mensagem,
            @Header("traceId") String traceId,
            @Header("spanId") String spanId
    ){
        TraceContext context = TraceContext.newBuilder()
                .traceId(Long.parseUnsignedLong(traceId, 16))
                .spanId(Long.parseUnsignedLong(spanId, 16))
                .sampled(true)
                .build();
        Span newSpan = tracer.newChild(context).name("comando-processar-regras").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(newSpan)){
            log.info("INICIO - Comando Processar Regras TraceId: {} SpanId: {} Mensagem: {}", traceId, spanId, mensagem);

            var comandoProcessarRegraDto = objectMapper.readValue(mensagem, ComandoProcessarRegraDto.class);
            processarRegrasEmLoteUseCase.executar(
                    comandoProcessarRegraDto.getUsuarioId(),
                    comandoProcessarRegraDto.getDespesaId(),
                    comandoProcessarRegraDto.getSequencialAtual(),
                    comandoProcessarRegraDto.getSequencialFinal()
            );

            log.info("FIM - Comando Processar Regras TraceId: {} SpanId: {} Mensagem: {}", traceId, spanId, mensagem);
        } catch (Exception exception) {
            log.error("ERRO: {} Mensagem: {}", exception.getMessage(), mensagem);
            throw new RuntimeException(exception);
        } finally {
            newSpan.finish();
        }
    }
}
