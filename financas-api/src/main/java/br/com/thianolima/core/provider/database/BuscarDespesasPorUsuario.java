package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.model.TipoDespesaEnum;
import br.com.thianolima.core.projection.DespesaPaginadaItemProjection;

import java.time.YearMonth;
import java.util.List;

public interface BuscarDespesasPorUsuario {

    List<DespesaPaginadaItemProjection> executar(
            Long usuarioId,
            YearMonth anomes,
            Long cartaoId,
            Long categoriaId,
            TipoDespesaEnum tipo
    );
}
