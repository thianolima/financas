package br.com.thianolima.entrypoint.controller.response;

import br.com.thianolima.core.model.Notificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoResponse {
    String tipo;
    String dataHoraCriacao;
    String mensagem;

    public NotificacaoResponse(Notificacao notificacao){
        this.tipo = notificacao.tipo();
        this.dataHoraCriacao = notificacao.dataHoraCriacao();
        this.mensagem = notificacao.mensagem();
    }
}
