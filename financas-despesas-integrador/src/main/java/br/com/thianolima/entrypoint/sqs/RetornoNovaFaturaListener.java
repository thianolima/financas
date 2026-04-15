package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.usecase.ProcessarRetornoNovaFaturaUseCase;
import br.com.thianolima.entrypoint.dto.RetornoFaturaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetornoNovaFaturaListener {
    private final ObjectMapper objectMapper;
    private final ProcessarRetornoNovaFaturaUseCase processarRetornoNovaFaturaUseCase;

    public RetornoNovaFaturaListener(
            ObjectMapper objectMapper,
            ProcessarRetornoNovaFaturaUseCase processarRetornoNovaFaturaUseCase
    ) {
        this.objectMapper = objectMapper;
        this.processarRetornoNovaFaturaUseCase = processarRetornoNovaFaturaUseCase;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.retorno-nova-fatura}", factory = "defaultSqsMessageListenerContainerFactory")
    public void receberMensagem(String mensagem){
        try {
            log.info("mensagem: {}", mensagem);
            RetornoFaturaDto retornoFaturaDto = objectMapper.readValue(mensagem, RetornoFaturaDto.class);
            processarRetornoNovaFaturaUseCase.executar(
                    retornoFaturaDto.getFaturaId(),
                    retornoFaturaDto.getSituacao(),
                    retornoFaturaDto.getDataConlusao()
            );
            log.info("retornoFaturaDto: {}", retornoFaturaDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
