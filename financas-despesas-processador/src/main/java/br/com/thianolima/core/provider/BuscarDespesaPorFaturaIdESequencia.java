package br.com.thianolima.core.provider;

import br.com.thianolima.model.Despesa;

import java.util.Optional;

public interface BuscarDespesaPorFaturaIdESequencia {

    Optional<Despesa> executar(
            Integer sequencia,
            Long faturaId
    );
}
