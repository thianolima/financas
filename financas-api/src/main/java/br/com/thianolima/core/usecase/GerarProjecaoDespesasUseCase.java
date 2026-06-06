package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.ProjecaoDespesaMensal;
import br.com.thianolima.core.model.ProjecaoDespesaMensalItens;
import br.com.thianolima.core.provider.database.BuscarDespesasRecorrenteDeCartaoPorUsuario;
import br.com.thianolima.core.provider.database.BuscarDespesasRecorrentePorUsuario;
import br.com.thianolima.core.provider.database.BuscarParcelasAtivasDeCartaoPorUsuario;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GerarProjecaoDespesasUseCase {

    private final BuscarParcelasAtivasDeCartaoPorUsuario buscarParcelasAtivasDeCartaoPorUsuario;
    private final BuscarDespesasRecorrenteDeCartaoPorUsuario buscarDespesasRecorrenteDeCartaoPorUsuario;
    private final BuscarDespesasRecorrentePorUsuario buscarDespesasRecorrentePorUsuario;

    public GerarProjecaoDespesasUseCase(
            BuscarParcelasAtivasDeCartaoPorUsuario buscarParcelasAtivasDeCartaoPorUsuario,
            BuscarDespesasRecorrenteDeCartaoPorUsuario buscarDespesasRecorrenteDeCartaoPorUsuario,
            BuscarDespesasRecorrentePorUsuario buscarDespesasRecorrentePorUsuario
    ){
        this.buscarParcelasAtivasDeCartaoPorUsuario = buscarParcelasAtivasDeCartaoPorUsuario;
        this.buscarDespesasRecorrenteDeCartaoPorUsuario = buscarDespesasRecorrenteDeCartaoPorUsuario;
        this.buscarDespesasRecorrentePorUsuario = buscarDespesasRecorrentePorUsuario;
    }

    public List<ProjecaoDespesaMensal> executar(
            Long usuarioId,
            Integer mesesProjecao
    ){
        Map<YearMonth, BigDecimal> mapValorTotalMes = new TreeMap<>();
        Map<YearMonth, List<ProjecaoDespesaMensalItens>> mapDespesasMes = new TreeMap<>();

        adicionarParcelasAtivasDeCartaoPorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);
        adicionarDespesasRecorrenteDeCartaoPorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);
        adicionarDespesasRecorrentePorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);

        return mapperDespesaMensal(mapValorTotalMes, mapDespesasMes);
    }

    private void adicionarParcelasAtivasDeCartaoPorUsuario(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItens>> mapDespesasMes
    ){
        buscarParcelasAtivasDeCartaoPorUsuario.executar(usuarioId).forEach(despesa -> {
            var proximaParcela = 1;
            var totalParcelasRestantes = despesa.getTotalParcelas() - despesa.getParcelaAtual();
            while(proximaParcela <= totalParcelasRestantes) {
                var anoMesProjetado = despesa.getDataVencimento().plusMonths(proximaParcela);

                mapValorTotalMes.merge(
                        YearMonth.from(anoMesProjetado),
                        despesa.getValor(),
                        BigDecimal::add
                );

                mapDespesasMes.computeIfAbsent(
                        YearMonth.from(anoMesProjetado),
                        despesas -> new ArrayList<>()
                ).add(
                        ProjecaoDespesaMensalItens.builder()
                        .usuarioId(despesa.getUsuarioId())
                        .descricaoOriginal(despesa.getDescricaoOriginal())
                        .descricaoProcessada(despesa.getDescricaoProcessada())
                        .categoriaId(despesa.getCategoriaId())
                        .categoriaNome(despesa.getCategoriaNome())
                        .cartaoId(despesa.getCartaoId())
                        .cartaoNome(despesa.getCartaoNome())
                        .parcelaAtual(despesa.getParcelaAtual() + proximaParcela)
                        .totalParcelas(despesa.getTotalParcelas())
                        .dataVencimento(despesa.getDataVencimento().plusMonths(proximaParcela))
                        .observacao(despesa.getObservacao())
                        .recorrente(despesa.isRecorrente())
                        .valor(despesa.getValor())
                        .build()
                );

                proximaParcela++;

                if(proximaParcela > mesesProjecao)
                    break;
            }
        });
    }

    private void adicionarDespesasRecorrenteDeCartaoPorUsuario(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItens>> mapDespesasMes
    ) {
        buscarDespesasRecorrenteDeCartaoPorUsuario.executar(usuarioId).forEach(despesa -> {
            for(int proximaParcela = 1; proximaParcela <= mesesProjecao; proximaParcela++) {
                var anoMesProjetado = despesa.getDataVencimento().plusMonths(proximaParcela);

                mapValorTotalMes.merge(
                        YearMonth.from(anoMesProjetado),
                        despesa.getValor(),
                        BigDecimal::add
                );

                mapDespesasMes.computeIfAbsent(
                        YearMonth.from(anoMesProjetado),
                        despesas -> new ArrayList<>()
                ).add(
                        ProjecaoDespesaMensalItens.builder()
                        .usuarioId(despesa.getUsuarioId())
                        .descricaoOriginal(despesa.getDescricaoOriginal())
                        .descricaoProcessada(despesa.getDescricaoProcessada())
                        .categoriaId(despesa.getCategoriaId())
                        .categoriaNome(despesa.getCategoriaNome())
                        .cartaoId(despesa.getCartaoId())
                        .cartaoNome(despesa.getCartaoNome())
                        .parcelaAtual(despesa.getParcelaAtual() + proximaParcela)
                        .totalParcelas(despesa.getTotalParcelas())
                        .dataVencimento(despesa.getDataVencimento().plusMonths(proximaParcela))
                        .observacao(despesa.getObservacao())
                        .recorrente(despesa.isRecorrente())
                        .valor(despesa.getValor())
                        .build()
                );
            }
        });
    }

    private void adicionarDespesasRecorrentePorUsuario(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItens>> mapDespesasMes
    ) {
        YearMonth anoMesAtual = YearMonth.now().minusMonths(1);
        YearMonth anoMesLimite = anoMesAtual.plusMonths(mesesProjecao).plusMonths(2);

        buscarDespesasRecorrentePorUsuario.executar(usuarioId).forEach(despesa -> {
            var anoMesDespesa = YearMonth.of(
                    despesa.getDataVencimento().getYear(),
                    despesa.getDataVencimento().getMonth()
            );
            if(anoMesDespesa.isAfter(anoMesAtual) && anoMesDespesa.isBefore(anoMesLimite)) {
                mapValorTotalMes.merge(
                        YearMonth.from(anoMesDespesa),
                        despesa.getValor(),
                        BigDecimal::add
                );

                mapDespesasMes.computeIfAbsent(
                        YearMonth.from(anoMesDespesa),
                        despesas -> new ArrayList<>()
                ).add(despesa);
            }
        });
    }

    private  List<ProjecaoDespesaMensal> mapperDespesaMensal(
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItens>> mapDespesasMes
    ){
        return mapValorTotalMes.entrySet().stream()
                .map(entry -> {
                    var despesasDoMes = mapDespesasMes.getOrDefault(entry.getKey(), List.of());

                    BigDecimal valorTotalParcelado = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItens::isParcelado)
                            .map(ProjecaoDespesaMensalItens::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal valorTotalRecorrente = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItens::isRecorrente)
                            .map(ProjecaoDespesaMensalItens::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal valorTotalAvulso = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItens::isAvulso)
                            .map(ProjecaoDespesaMensalItens::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return ProjecaoDespesaMensal.builder()
                            .anoMes(entry.getKey())
                            .valorTotal(entry.getValue())
                            .valorTotalParcelado(valorTotalParcelado)
                            .valorTotalRecorrente(valorTotalRecorrente)
                            .valorTotalAvulso(valorTotalAvulso)
                            .despesas(mapDespesasMes.getOrDefault(entry.getKey(), List.of()))
                            .build();
                }).toList();
    }
}
