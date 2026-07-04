package br.com.thianolima.entrypoint.sqs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.annotations.NotNull;

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
}
