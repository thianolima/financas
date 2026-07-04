package br.com.thianolima.infrastructure.dabatase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

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

    @DynamoDbPartitionKey
    @DynamoDbAttribute("notificacao_id")
    public String getId() {
        return id;
    }

    @DynamoDbSortKey
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

    @DynamoDbAttribute("usuario_id")
    public Long getUsuarioId() {
        return usuarioId;
    }
}
