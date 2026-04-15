package br.com.thianolima.core.provider;

import br.com.thianolima.model.Fatura;

import java.util.Optional;

public interface BuscarFaturaPorCartaoIdEAnoMes {

    Optional<Fatura> executar(Long cartaoId, String anoMes);
}
