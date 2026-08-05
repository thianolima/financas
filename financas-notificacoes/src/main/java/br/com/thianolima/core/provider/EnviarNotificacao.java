package br.com.thianolima.core.provider;

import br.com.thianolima.core.projection.Notificacao;

public interface EnviarNotificacao {

    void executar(Notificacao notificacao);
}
