package br.com.thianolima.core.provider;

import br.com.thianolima.core.model.Notificacao;

public interface EnviarNotificacao {

    void executar(Notificacao notificacao);
}
