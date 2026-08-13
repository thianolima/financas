package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.DashboardItemCategoriaProjection;

import java.time.LocalDate;
import java.util.List;

public interface BuscarTotaisDespesasPorCategoriaDashboard {

    List<DashboardItemCategoriaProjection> executar(LocalDate dataReferencia, Long usuarioId);
}
