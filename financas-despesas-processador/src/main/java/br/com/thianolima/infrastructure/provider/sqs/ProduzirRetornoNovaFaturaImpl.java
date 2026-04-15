package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.provider.ProduzirRetornoNovaFatura;
import br.com.thianolima.infrastructure.provider.sqs.dto.RetornoFaturaDto;
import br.com.thianolima.model.FaturaSituacaoEnum;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProduzirRetornoNovaFaturaImpl implements ProduzirRetornoNovaFatura {

    private final SqsTemplate sqsTemplate;
    private final String nomeFila;

    public ProduzirRetornoNovaFaturaImpl(
            SqsTemplate sqsTemplate,
            @Value("${spring.cloud.aws.sqs.queue.retorno-nova-fatura}")
            String nomeFila
    ) {
        this.sqsTemplate = sqsTemplate;
        this.nomeFila = nomeFila;
    }

    @Override
    public boolean executar(Long faturaId) {
        var retornoFaturaDto = RetornoFaturaDto.builder()
                .faturaId(faturaId)
                .situacao(FaturaSituacaoEnum.CONCLUIDO)
                .dataConlusao(LocalDateTime.now())
                .build();
        sqsTemplate.send(options -> options
                .queue(nomeFila)
                .payload(retornoFaturaDto)
        );
        log.info(retornoFaturaDto.toString());
        return true;
    }
}
