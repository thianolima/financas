package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.model.DespesaPaginadaItem;
import br.com.thianolima.core.model.TipoDespesaEnum;

import java.time.YearMonth;
import java.util.List;

public interface BuscarDespesasPorUsuario {

    List<DespesaPaginadaItem> executar(
            Long usuarioId,
            YearMonth anomes,
            Long cartaoId,
            Long categoriaId,
            TipoDespesaEnum tipo
    );
}
