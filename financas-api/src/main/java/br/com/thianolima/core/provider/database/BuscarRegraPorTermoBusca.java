package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Regra;

import java.util.Optional;

public interface BuscarRegraPorTermoBusca {

    Optional<Regra> executar(String termoBusca, Long usuarioId);
}
