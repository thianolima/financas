package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarRetornoNovaFaturaUseCase;
import br.com.thianolima.entrypoint.dto.RetornoFaturaDto;
import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetornoNovaFaturaListener {
    private final ObjectMapper objectMapper;
    private final ProcessarRetornoNovaFaturaUseCase processarRetornoNovaFaturaUseCase;
    private final Tracer tracer;

    public RetornoNovaFaturaListener(
            ObjectMapper objectMapper,
            ProcessarRetornoNovaFaturaUseCase processarRetornoNovaFaturaUseCase,
            Tracer tracer
    ) {
        this.objectMapper = objectMapper;
        this.processarRetornoNovaFaturaUseCase = processarRetornoNovaFaturaUseCase;
        this.tracer = tracer;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.retorno-nova-fatura}", factory = "defaultSqsMessageListenerContainerFactory")
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

        Span newSpan = tracer.newChild(context).name("retorno-nova-fatura").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(newSpan)){
            RetornoFaturaDto retornoFaturaDto = objectMapper.readValue(mensagem, RetornoFaturaDto.class);

            processarRetornoNovaFaturaUseCase.executar(
                    retornoFaturaDto.getFaturaId(),
                    retornoFaturaDto.getSituacao(),
                    retornoFaturaDto.getDataConlusao()
            );

            log.info("Sucesso retornoFaturaDto: {}", retornoFaturaDto);
        } catch (JsonProcessingException exception) {
            log.error("Erro: {} Mensagem: {}", exception.getMessage(), mensagem);
            throw new RuntimeException(exception);
        } finally {
            newSpan.finish();
        }
    }
}
