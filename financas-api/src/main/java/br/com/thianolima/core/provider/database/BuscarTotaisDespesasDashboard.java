package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.DashboardItemTotaisProjection;

import java.time.LocalDate;
import java.util.List;

public interface BuscarTotaisDespesasDashboard {
    List<DashboardItemTotaisProjection> executar(LocalDate dataReferencia, Long usuarioId);

}
