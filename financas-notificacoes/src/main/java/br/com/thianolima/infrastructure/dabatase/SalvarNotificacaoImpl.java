package br.com.thianolima.infrastructure.dabatase;

import br.com.thianolima.core.model.Notificacao;
import br.com.thianolima.core.provider.SalvarNotificacao;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class SalvarNotificacaoImpl implements SalvarNotificacao {

    private final DynamoDbEnhancedClient enhancedClient;

    public SalvarNotificacaoImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @Override
    public void executar(Notificacao notificacao) {
        String notificacaoId = (notificacao.id() != null && !notificacao.id().isBlank())
                ? notificacao.id() : java.util.UUID.randomUUID().toString();

        long dataExpuroEmSegundos = LocalDate.now()
                .plusDays(7)
                .atStartOfDay(ZoneId.of("America/Sao_Paulo"))
                .toEpochSecond();

        String dataHoraCriacao = notificacao.dataHoraCriacao() != null ?
                notificacao.dataHoraCriacao() : LocalDateTime.now().toString();

        NotificacaoEntity entidade = NotificacaoEntity.builder()
                .id(notificacaoId)
                .dataHoraCriacao(dataHoraCriacao)
                .usuarioId(notificacao.usuarioId())
                .tipo(notificacao.tipo())
                .mensagem(notificacao.mensagem())
                .dataExpurgo(dataExpuroEmSegundos)
                .build();

        DynamoDbTable<NotificacaoEntity> table = enhancedClient.table(
                NotificacaoEntity.NOME_TABELA,
                TableSchema.fromBean(NotificacaoEntity.class)
        );

        table.putItem(entidade);
    }
}
