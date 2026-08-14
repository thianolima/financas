package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.DashboardItemHistoricoProjection;

import java.time.LocalDate;
import java.util.List;

public interface BuscarTotaisDespesasPorHistoricoDashboard {

    List<DashboardItemHistoricoProjection> executar(LocalDate dataReferencia, Long usuarioId);

}
