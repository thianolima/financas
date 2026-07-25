package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarComandoNovaDespesaUseCase;
import br.com.thianolima.entrypoint.dto.ComandoNovaDespesaDto;
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
    private final ProcessarComandoNovaDespesaUseCase processarComandoNovaDespesaUseCase;
    private final Tracer tracer;

    public ComandoNovaDespesaListener(
            ObjectMapper objectMapper,

            ProcessarComandoNovaDespesaUseCase processarComandoNovaDespesaUseCase,
            Tracer tracer
    ) {
        this.objectMapper = objectMapper;
        this.processarComandoNovaDespesaUseCase = processarComandoNovaDespesaUseCase;
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
            log.info("INICIO - Comando Nova Despesa Listener: {}", mensagem);
            var comandoNovaDespesaDto = objectMapper.readValue(mensagem, ComandoNovaDespesaDto.class);
            processarComandoNovaDespesaUseCase.executar(
                    comandoNovaDespesaDto.toDespesa(),
                    comandoNovaDespesaDto.getSequencialAtual(),
                    comandoNovaDespesaDto.getSequencialFinal()
            );
            log.info("FIM - Comando Nova Despesa Listener: {}", mensagem);
        } catch (Exception exception) {
            log.error("ERRO: {} Mensagem: {}", exception.getMessage(), mensagem);
            throw new RuntimeException(exception);
        } finally {
            newSpan.finish();
        }
    }
}
