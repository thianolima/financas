package br.com.thianolima.core.provider;

import br.com.thianolima.model.Despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface BuscarParcelaAnterior {

    Optional<Despesa> executar(Despesa despesa);

}
