package br.com.thianolima.core.usecase;

import br.com.thianolima.core.projection.Notificacao;
import br.com.thianolima.core.provider.EnviarNotificacao;
import br.com.thianolima.core.provider.SalvarNotificacao;

public class CriarNovaNotificacaoUseCase {

    private final SalvarNotificacao salvarNotificacao;
    private final EnviarNotificacao enviarNotificacao;

    public CriarNovaNotificacaoUseCase(
            SalvarNotificacao salvarNotificacao,
            EnviarNotificacao enviarNotificacao
    ) {
        this.salvarNotificacao = salvarNotificacao;
        this.enviarNotificacao = enviarNotificacao;
    }

    public void executar(Notificacao notificacao) {
        salvarNotificacao.executar(notificacao);
        enviarNotificacao.executar(notificacao);
    }
}
