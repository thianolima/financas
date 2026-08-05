package br.com.thianolima.entrypoint.sqs.dto;

import br.com.thianolima.core.projection.Notificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NotificacaoDto {
    @NotNull
    Long usuarioId;

    @NotNull
    String tipo;

    @NotNull
    String mensagem;

    public Notificacao toModel(){
        return new Notificacao(
                null,
                usuarioId,
                tipo,
                LocalDateTime.now().toString(),
                mensagem
        );

    }
}
