package br.com.thianolima.core.provider;

import br.com.thianolima.model.Fatura;

import java.util.Optional;

public interface BuscarFaturaPorId {

    Optional<Fatura> executar(Long faturaId);
}
