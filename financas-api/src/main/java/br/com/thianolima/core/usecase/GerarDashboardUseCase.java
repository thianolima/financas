package br.com.thianolima.core.usecase;

import br.com.thianolima.core.projection.DashboardItemHistoricoProjection;
import br.com.thianolima.core.projection.DashboardProjection;
import br.com.thianolima.core.provider.database.BuscarLimiteUtilizadoCartao;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasDashboard;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasPorCategoriaDashboard;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasPorHistoricoDashboard;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class GerarDashboardUseCase {

    private final BuscarTotaisDespesasPorCategoriaDashboard buscarTotaisDespesasPorCategoriaDashboard;
    private final BuscarTotaisDespesasDashboard buscarTotaisDespesasDashboard;
    private final BuscarLimiteUtilizadoCartao buscarLimiteUtilizadoCartao;
    private final BuscarTotaisDespesasPorHistoricoDashboard buscarTotaisDespesasPorHistoricoDashboard;

    private final GerarProjecaoDespesasUseCase gerarProjecaoDespesasUseCase;

    public GerarDashboardUseCase(
            BuscarTotaisDespesasPorCategoriaDashboard buscarTotaisDespesasPorCategoriaDashboard,
            BuscarTotaisDespesasDashboard buscarTotaisDespesasDashboard,
            BuscarLimiteUtilizadoCartao buscarLimiteUtilizadoCartao,
            BuscarTotaisDespesasPorHistoricoDashboard buscarTotaisDespesasPorHistoricoDashboard,
            GerarProjecaoDespesasUseCase gerarProjecaoDespesasUseCase
    ) {
        this.buscarTotaisDespesasPorCategoriaDashboard = buscarTotaisDespesasPorCategoriaDashboard;
        this.buscarTotaisDespesasDashboard = buscarTotaisDespesasDashboard;
        this.buscarLimiteUtilizadoCartao = buscarLimiteUtilizadoCartao;
        this.buscarTotaisDespesasPorHistoricoDashboard = buscarTotaisDespesasPorHistoricoDashboard;
        this.gerarProjecaoDespesasUseCase = gerarProjecaoDespesasUseCase;
    }

    public DashboardProjection executar(LocalDate dataReferencia, Long usuarioId) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var futureCategorias = executor.submit(
                    () -> buscarTotaisDespesasPorCategoriaDashboard.executar(dataReferencia, usuarioId)
            );

            var futureTotais = executor.submit(
                    () -> buscarTotaisDespesasDashboard.executar(dataReferencia, usuarioId)
            );

            var futureLimites = executor.submit(
                    () -> buscarLimiteUtilizadoCartao.executar(null, usuarioId)
            );

            var futureHistorico = executor.submit(
                    () -> gerarHistoricoDespesas(dataReferencia, usuarioId)
            );

            return DashboardProjection.builder()
                .cardDespesasPorCategoria(futureCategorias.get())
                .cardTotaisDespesas(futureTotais.get())
                .cardLimitesCartoes(futureLimites.get())
                .cardDespesasPorHistorico(futureHistorico.get())
                .build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar dashboard", e);
        }
    }

    private List<DashboardItemHistoricoProjection> gerarHistoricoDespesas(LocalDate dataReferencia, Long usuarioId) {
        var isMesReferenciaIgualAtual =
                dataReferencia.getMonth().equals(LocalDate.now().getMonth()) &&
                dataReferencia.getYear() == LocalDate.now().getYear();

        if(isMesReferenciaIgualAtual){
            var totaisRealizado = buscarTotaisDespesasPorHistoricoDashboard.executar(dataReferencia, usuarioId);
            var totaisProjetado = gerarProjecaoDespesasUseCase.executar(usuarioId, 1);

            return List.of(
                    totaisRealizado.get(0),
                    totaisRealizado.get(1),
                    DashboardItemHistoricoProjection.builder()
                        .mes(totaisProjetado.getFirst().getAnoMes().getMonthValue())
                        .valorTotal(totaisProjetado.getFirst().getValorTotal())
                        .projecao(true)
                        .build()
            );
        }
        return buscarTotaisDespesasPorHistoricoDashboard.executar(dataReferencia, usuarioId);
    }
}
