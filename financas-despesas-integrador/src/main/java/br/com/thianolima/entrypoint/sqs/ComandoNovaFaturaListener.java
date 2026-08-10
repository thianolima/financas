package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.provider.CarregarFaturaExcel;
import br.com.thianolima.core.usecase.ProcessarFaturaExcelUseCase;
import br.com.thianolima.entrypoint.dto.S3EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ComandoNovaFaturaListener {

    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final ProcessarFaturaExcelUseCase processarFaturaExcelUseCase;

    public ComandoNovaFaturaListener(
            ObjectMapper objectMapper,
            Tracer tracer,
            CarregarFaturaExcel carregarFaturaExcelImpl,
            ProcessarFaturaExcelUseCase processarFaturaExcelUseCase
    ) {
        this.objectMapper = objectMapper;
        this.tracer = tracer;
        this.processarFaturaExcelUseCase = processarFaturaExcelUseCase;
    }

    @SqsListener(
            value = "${spring.cloud.aws.sqs.queue.comando-nova-fatura}",
            factory = "defaultSqsMessageListenerContainerFactory"
    )
    public void receberMensagem(String mensagem){
        ScopedSpan span = tracer.startScopedSpan("comando-nova-fatura");

        try {
            log.info("INICIO - Comando Nova Fatura Listener mensagem: {}", mensagem);

            S3EventDto s3EventDto = objectMapper.readValue(mensagem, S3EventDto.class);
            var splitKey = s3EventDto.records().getFirst().s3().object().key().split("/");
            var usuarioId = Long.parseLong(splitKey[0]);
            var cartaoId = Long.parseLong(splitKey[1]);
            var anomes = splitKey[2];
            var s3Bucket = s3EventDto.records().getFirst().s3().bucket().name();
            var s3Key = s3EventDto.records().getFirst().s3().object().key();

            processarFaturaExcelUseCase.executar(
                    usuarioId,
                    cartaoId,
                    anomes,
                    s3Bucket,
                    s3Key
            );

            log.info("FIM - Comando Nova Fatura Listener mensagem: {}", mensagem);
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }


}
