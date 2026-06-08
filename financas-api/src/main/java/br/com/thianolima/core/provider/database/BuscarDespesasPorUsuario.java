package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.model.DespesaPaginadaItem;

import java.time.YearMonth;
import java.util.List;

public interface BuscarDespesasPorUsuario {

    List<DespesaPaginadaItem> executar(Long usuarioId, YearMonth anomes);
}
