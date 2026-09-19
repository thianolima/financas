package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.DashboardItemTotaisParcelasProjection;

import java.time.LocalDate;
import java.util.List;

public interface BuscarTotaisPrimeiraUltimaParcelaDashboard {

    List<DashboardItemTotaisParcelasProjection> executar(LocalDate dataReferencia, Long usuarioId);
}
