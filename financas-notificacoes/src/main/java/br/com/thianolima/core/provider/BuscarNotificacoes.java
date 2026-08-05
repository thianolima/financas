package br.com.thianolima.core.provider;

import br.com.thianolima.core.projection.Notificacao;

import java.util.List;

public interface BuscarNotificacoes {

    List<Notificacao> executar(Long usuarioId);
}
