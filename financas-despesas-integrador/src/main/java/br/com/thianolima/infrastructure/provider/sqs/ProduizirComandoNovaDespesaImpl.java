package br.com.thianolima.infrastructure.provider.sqs;

import br.com.thianolima.core.dto.DespesaCsvDto;
import br.com.thianolima.core.provider.ProduzirComandoNovaDespesa;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.eventstream.MessageBuilder;

@Slf4j
@Service
public class ProduizirComandoNovaDespesaImpl implements ProduzirComandoNovaDespesa {

    private final SqsTemplate sqsTemplate;
    private final String nomeFila;

    public ProduizirComandoNovaDespesaImpl(
            SqsTemplate sqsTemplate,
            @Value("${spring.cloud.aws.sqs.queue.comando-nova-despesa}")
            String nomeFila
    ) {
        this.sqsTemplate = sqsTemplate;
        this.nomeFila = nomeFila;
    }

    @Override
    public boolean executar(DespesaCsvDto despesa) {
        log.info(
                "sequencia: {} data: {} descricao: {} valor: {} fatura_id: {}",
                despesa.getSequencia(), despesa.getData(), despesa.getDescricao(), despesa.getValor(), despesa.getFaturaId()
        );

        sqsTemplate.send(options -> options
                .queue(nomeFila)
                .payload(despesa)
                .messageGroupId(despesa.getFaturaId().toString())
                .messageDeduplicationId(despesa.getFaturaId() + "-" + despesa.getSequencia())
        );

        return true;
    }
}
