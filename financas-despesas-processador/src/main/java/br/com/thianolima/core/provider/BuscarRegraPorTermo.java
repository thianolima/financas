package br.com.thianolima.core.provider;

import br.com.thianolima.model.Regra;

import java.util.Optional;

public interface BuscarRegraPorTermo {

    Optional<Regra> executar(String termoBusca, Long usuarioId);
}
