package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarParcelasAtivasDeCartaoPorUsuario;
import br.com.thianolima.model.DespesaMensal;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GerarProjecaoDespesasUseCase {

    private final BuscarParcelasAtivasDeCartaoPorUsuario buscarDespesasParceladas;

    public GerarProjecaoDespesasUseCase(
            BuscarParcelasAtivasDeCartaoPorUsuario buscarDespesasParceladas
    ){
        this.buscarDespesasParceladas = buscarDespesasParceladas;
    }

    public List<DespesaMensal> executar(
            Long usuarioId,
            Integer mesesProjecao
    ){
        var resultado = buscarDespesasParceladas.executar(usuarioId);

        YearMonth anoMesAtual = YearMonth.now();
        YearMonth anoMesLimite = anoMesAtual.plusMonths(mesesProjecao + 1);

        Map<YearMonth, BigDecimal> agregacao = new TreeMap<>();
        resultado.forEach(despesa -> {
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

        return agregacao.entrySet().stream()
                .map(entry -> new DespesaMensal(entry.getKey(), entry.getValue()))
                .toList();
    }
}
