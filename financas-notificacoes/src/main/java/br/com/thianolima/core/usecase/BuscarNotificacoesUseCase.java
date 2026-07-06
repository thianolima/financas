package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.Notificacao;
import br.com.thianolima.core.provider.BuscarNotificacoes;

import java.util.List;

public class BuscarNotificacoesUseCase {

    private final BuscarNotificacoes buscarNotificacoes;

    public BuscarNotificacoesUseCase(BuscarNotificacoes buscarNotificacoes) {
        this.buscarNotificacoes = buscarNotificacoes;
    }

    public List<Notificacao> executar(
            Long usuarioId
    ){
        return buscarNotificacoes.executar(usuarioId);
    }
}
