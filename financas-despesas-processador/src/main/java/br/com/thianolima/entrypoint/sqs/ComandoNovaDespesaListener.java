package br.com.thianolima.entrypoint.sqs;

import br.com.thianolima.core.dto.DespesaCsvDto;
import br.com.thianolima.core.usecase.ProcessarComandoNovaDespesaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ComandoNovaDespesaListener {

    private final ObjectMapper objectMapper;
    private final ProcessarComandoNovaDespesaUseCase processarComandoNovaDespesaUseCase;

    public ComandoNovaDespesaListener(
            ObjectMapper objectMapper,
            ProcessarComandoNovaDespesaUseCase processarComandoNovaDespesaUseCase
    ) {
        this.objectMapper = objectMapper;
        this.processarComandoNovaDespesaUseCase = processarComandoNovaDespesaUseCase;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.comando-nova-despesa}", factory = "defaultSqsMessageListenerContainerFactory")
    public void receberMensagem(String mensagem){
        try {
            var despesaCsv = objectMapper.readValue(mensagem, DespesaCsvDto.class);
            processarComandoNovaDespesaUseCase.executar(despesaCsv);
            log.info(mensagem.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
