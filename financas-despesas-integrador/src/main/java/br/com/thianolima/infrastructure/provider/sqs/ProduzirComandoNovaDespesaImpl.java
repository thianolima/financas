package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.dto.FaturaItemDto;
import br.com.thianolima.core.provider.ProduzirComandoNovaDespesa;
import br.com.thianolima.infrastructure.provider.sqs.dto.ComandoNovaDespesaDto;
import br.com.thianolima.model.Despesa;
import brave.Tracer;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public void executar(
            Despesa despesa,
            int sequencialAtual,
            int sequenciaFinal
    ) {
        try {
            var currentSpan = tracer.currentSpan();
            var traceId = currentSpan.context().traceIdString();
            var spanId = currentSpan.context().spanIdString();

            var novaDespesa = ComandoNovaDespesaDto.builder()
                    .usuarioId(despesa.getUsuarioId())
                    .cartaoId(despesa.getCartaoId())
                    .faturaId(despesa.getFaturaId())
                    .dataDespesa(despesa.getDataDespesa())
                    .dataVencimento(despesa.getDataVencimento())
                    .descricao(despesa.getDescricaoOriginal())
                    .valor(despesa.getValor())
                    .parcelaAtual(despesa.getParcelaAtual() != null ? despesa.getParcelaAtual() : 0)
                    .totalParcelas(despesa.getTotalParcelas() != null ? despesa.getTotalParcelas() : 0)
                    .sequencialAtual(sequencialAtual)
                    .sequencialFinal(sequenciaFinal)
                    .build();

            log.info("TraceId: {} SpanId: {} Mensagem: {}", traceId, spanId, novaDespesa);

            sqsTemplate.send(options -> options
                    .queue(nomeFila)
                    .payload(novaDespesa)
                    .header("traceId", traceId)
                    .header("spanId", spanId)
                    .messageGroupId(traceId)
                    .messageDeduplicationId(UUID.randomUUID().toString())
            );
        }catch (Exception e){
            log.error("Erro ao enviar mensagem para a fila SQS: {}", e.getMessage(), e);
        }

    }
}
