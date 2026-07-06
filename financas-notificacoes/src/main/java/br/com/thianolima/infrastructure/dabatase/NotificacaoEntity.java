package br.com.thianolima.infrastructure.dabatase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class NotificacaoEntity {
    private String id;
    private String dataHoraCriacao;
    private Long usuarioId;
    private String tipo;
    private String mensagem;
    private Long dataExpurgo;

    public static final String NOME_TABELA = "notificacoes";
    public static final String INDICE_USUARIO = "idx_notificacoes_usuario";

    @DynamoDbPartitionKey
    @DynamoDbAttribute("notificacao_id")
    public String getId() {
        return id;
    }

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = NotificacaoEntity.INDICE_USUARIO)
    @DynamoDbAttribute("data_hora_criacao")
    public String getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    @DynamoDbAttribute("data_expurgo")
    public Long getDataExpurgo() {
        return dataExpurgo;
    }

    @DynamoDbAttribute("mensagem")
    public String getMensagem() {
        return mensagem;
    }

    @DynamoDbAttribute("tipo")
    public String getTipo() {
        return tipo;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = NotificacaoEntity.INDICE_USUARIO)
    @DynamoDbAttribute("usuario_id")
    public Long getUsuarioId() {
        return usuarioId;
    }
}
