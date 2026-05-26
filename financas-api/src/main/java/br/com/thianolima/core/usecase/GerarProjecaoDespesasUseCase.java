package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarDespesasRecorrenteDeCartaoPorUsuario;
import br.com.thianolima.core.provider.database.BuscarParcelasAtivasDeCartaoPorUsuario;
import br.com.thianolima.core.model.DespesaMensal;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GerarProjecaoDespesasUseCase {

    private final BuscarParcelasAtivasDeCartaoPorUsuario buscarParcelasAtivasDeCartaoPorUsuario;
    private final BuscarDespesasRecorrenteDeCartaoPorUsuario buscarDespesasRecorrenteDeCartaoPorUsuario;

    public GerarProjecaoDespesasUseCase(
            BuscarParcelasAtivasDeCartaoPorUsuario buscarParcelasAtivasDeCartaoPorUsuario,
            BuscarDespesasRecorrenteDeCartaoPorUsuario buscarDespesasRecorrenteDeCartaoPorUsuario
    ){
        this.buscarParcelasAtivasDeCartaoPorUsuario = buscarParcelasAtivasDeCartaoPorUsuario;
        this.buscarDespesasRecorrenteDeCartaoPorUsuario = buscarDespesasRecorrenteDeCartaoPorUsuario;
    }

    public List<DespesaMensal> executar(
            Long usuarioId,
            Integer mesesProjecao
    ){
        YearMonth anoMesAtual = YearMonth.now();
        YearMonth anoMesLimite = anoMesAtual.plusMonths(mesesProjecao + 1);
        Map<YearMonth, BigDecimal> agregacao = new TreeMap<>();

        var resultadoParcelasAtivasCartao = buscarParcelasAtivasDeCartaoPorUsuario.executar(usuarioId);
        resultadoParcelasAtivasCartao.forEach(despesa -> {
            Integer parcelasRestantes = (despesa.getTotalParcelas() - despesa.getParcelaAtual());
            while(parcelasRestantes > 0) {
                YearMonth anoMesProjetado = YearMonth.from(despesa.getDataVencimento().plusMonths(parcelasRestantes));
                if (anoMesProjetado.isAfter(anoMesAtual) && anoMesProjetado.isBefore(anoMesLimite)) {
                    agregacao.merge(
                            YearMonth.from(despesa.getDataVencimento().plusMonths(parcelasRestantes)),
                            despesa.getValor(),
                            BigDecimal::add
                    );
                }
                parcelasRestantes--;
            }
        });

        var resultadoDespesasRecorrentesCartao = buscarDespesasRecorrenteDeCartaoPorUsuario.executar(usuarioId);
        resultadoDespesasRecorrentesCartao.forEach(despesa -> {
            YearMonth anoMesProjetado = anoMesAtual.plusMonths(1);
            while(anoMesProjetado.isBefore(anoMesLimite)) {
                agregacao.merge(
                    YearMonth.from(anoMesProjetado),
                    despesa.getValor(),
                    BigDecimal::add
                );
                anoMesProjetado = anoMesProjetado.plusMonths(1);
            }
        });

        return agregacao.entrySet().stream()
                .map(entry -> new DespesaMensal(entry.getKey(), entry.getValue()))
                .toList();
    }
}
