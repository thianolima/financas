package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarComandoNovaFaturaUseCase;
import br.com.thianolima.entrypoint.dto.S3EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ComandoNovaFaturaListener {

    private final ObjectMapper objectMapper;
    private final ProcessarComandoNovaFaturaUseCase processarComandoNovaFaturaUseCase;

    public ComandoNovaFaturaListener(
            ObjectMapper objectMapper,
            ProcessarComandoNovaFaturaUseCase processarComandoNovaFaturaUseCase
    ) {
        this.objectMapper = objectMapper;
        this.processarComandoNovaFaturaUseCase = processarComandoNovaFaturaUseCase;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.comando-nova-fatura}", factory = "defaultSqsMessageListenerContainerFactory")
    public void receberMensagem(String mensagem){
        try {
            log.info("mensagem: {}", mensagem);

            S3EventDto s3EventDto = objectMapper.readValue(mensagem, S3EventDto.class);

            var splitKey = s3EventDto.getRecords().getFirst().getS3().getObject().getKey().split("/");
            var usuarioId = Long.parseLong(splitKey[0]);
            var cartaoId = Long.parseLong(splitKey[1]);
            var anomes = splitKey[2];
            var s3Bucket = s3EventDto.getRecords().getFirst().getS3().getBucket().getName();
            var s3Key = s3EventDto.getRecords().getFirst().getS3().getObject().getKey();

            processarComandoNovaFaturaUseCase.executar(
                    usuarioId,
                    cartaoId,
                    anomes,
                    s3Bucket,
                    s3Key
            );

        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
