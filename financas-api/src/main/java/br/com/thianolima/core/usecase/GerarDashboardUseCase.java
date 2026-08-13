package br.com.thianolima.core.usecase;

import br.com.thianolima.core.projection.DashboardProjection;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasPorCategoriaDashboard;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasDashboard;

import java.time.LocalDate;

public class GerarDashboardUseCase {

    private final BuscarTotaisDespesasPorCategoriaDashboard buscarTotaisDespesasPorCategoriaDashboard;
    private final BuscarTotaisDespesasDashboard buscarTotaisDespesasDashboard;

    public GerarDashboardUseCase(
            BuscarTotaisDespesasPorCategoriaDashboard buscarTotaisDespesasPorCategoriaDashboard,
            BuscarTotaisDespesasDashboard buscarTotaisDespesasDashboard
    ) {
        this.buscarTotaisDespesasPorCategoriaDashboard = buscarTotaisDespesasPorCategoriaDashboard;
        this.buscarTotaisDespesasDashboard = buscarTotaisDespesasDashboard;
    }

    public DashboardProjection executar(LocalDate dataReferencia, Long usuarioId) {
        var despesasCategoria = buscarTotaisDespesasPorCategoriaDashboard.executar(dataReferencia, usuarioId);
        var ranking = despesasCategoria.subList(0, 5);
        var totais = buscarTotaisDespesasDashboard.executar(dataReferencia, usuarioId);

        return DashboardProjection.builder()
                .cardDespesasPorCategoria(despesasCategoria)
                .cardRankingCategorias(ranking)
                .cardTotaisDespesas(totais)
                .build();

    }
}
