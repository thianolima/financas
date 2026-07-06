package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.Notificacao;
import br.com.thianolima.core.provider.SalvarNotificacao;

import java.time.LocalDateTime;

public class CriarNovaNotificacaoUseCase {

    private final SalvarNotificacao salvarNotificacao;

    public CriarNovaNotificacaoUseCase(
            SalvarNotificacao salvarNotificacao
    ) {
        this.salvarNotificacao = salvarNotificacao;
    }

    public void executar(Notificacao notificacao) {
        salvarNotificacao.executar(notificacao);
    }
}
