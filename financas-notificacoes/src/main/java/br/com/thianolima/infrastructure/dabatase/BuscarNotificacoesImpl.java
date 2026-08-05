package br.com.thianolima.infrastructure.dabatase;

import br.com.thianolima.core.projection.Notificacao;
import br.com.thianolima.core.provider.BuscarNotificacoes;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarNotificacoesImpl implements BuscarNotificacoes {

    private final DynamoDbEnhancedClient enhancedClient;

    public BuscarNotificacoesImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @Override
    public List<Notificacao> executar(Long usuarioId) {
        // 1. Obtém a referência estrita à tabela física "notificacao" usando o Schema da sua entidade
        DynamoDbTable<NotificacaoEntity> tabela = enhancedClient.table(
                NotificacaoEntity.NOME_TABELA,
                TableSchema.fromBean(NotificacaoEntity.class)
        );

        // 2. Aponta especificamente para o índice físico criado no DynamoDB via Terraform
        DynamoDbIndex<NotificacaoEntity> indice = tabela.index(NotificacaoEntity.INDICE_USUARIO);

        // 3. Define a chave de busca baseada no Partition Key do GSI (usuario_id)
        Key chaveDeBusca = Key.builder()
                .partitionValue(usuarioId)
                .build();

        // 4. Constrói a requisição ordenando de forma decrescente (scanIndexForward = false)
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(chaveDeBusca))
                .scanIndexForward(false)
                .build();

        // 5. Executa a query diretamente no índice
        var resultadoPages = indice.query(queryRequest);

        // 6. Converte as páginas de retorno em uma lista plana de entidades
        return resultadoPages.stream()
                .flatMap(page -> page.items().stream())
                .map(entity ->
                        new Notificacao(
                            entity.getId(),
                            entity.getUsuarioId(),
                            entity.getTipo(),
                            entity.getDataHoraCriacao(),
                            entity.getMensagem()
                        )
                )
                .collect(Collectors.toList());
    }
}
