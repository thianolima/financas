package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Regra;

import java.util.Optional;

public interface BuscarRegraPorId {
    Optional<Regra> executar(Long regraId, Long usuarioId);
}

