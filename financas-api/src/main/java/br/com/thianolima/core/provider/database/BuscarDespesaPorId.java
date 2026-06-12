package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Despesa;

import java.util.Optional;

public interface BuscarDespesaPorId {
    Optional<Despesa> executar(Long despesaId, Long usuarioId);
}

