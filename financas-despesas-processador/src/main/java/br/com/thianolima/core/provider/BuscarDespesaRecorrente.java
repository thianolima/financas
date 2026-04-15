package br.com.thianolima.core.provider;

import br.com.thianolima.model.Despesa;

import java.math.BigDecimal;
import java.util.Optional;

public interface BuscarDespesaRecorrente {

    Optional<Despesa> executar(
            String descricaoOriginal,
            BigDecimal valor,
            Long cartaoId
    );
}
