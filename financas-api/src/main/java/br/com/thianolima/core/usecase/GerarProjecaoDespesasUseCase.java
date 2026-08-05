package br.com.thianolima.core.usecase;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalItensProjection;
import br.com.thianolima.core.projection.ProjecaoDespesaMensalProjection;
import br.com.thianolima.core.provider.database.BuscarDespesasFuturas;
import br.com.thianolima.core.provider.database.BuscarDespesasRecorrenteDeCartao;
import br.com.thianolima.core.provider.database.BuscarParcelasAtivasDeCartao;
import br.com.thianolima.core.provider.database.BuscarProjecaoDespesasPorCategoria;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GerarProjecaoDespesasUseCase {

    private final BuscarParcelasAtivasDeCartao buscarParcelasAtivasDeCartao;
    private final BuscarDespesasRecorrenteDeCartao buscarDespesasRecorrenteDeCartao;
    private final BuscarDespesasFuturas buscarDespesasFuturas;
    private final BuscarProjecaoDespesasPorCategoria buscarProjecaoDespesasPorCategoria;

    public GerarProjecaoDespesasUseCase(
            BuscarParcelasAtivasDeCartao buscarParcelasAtivasDeCartao,
            BuscarDespesasRecorrenteDeCartao buscarDespesasRecorrenteDeCartao,
            BuscarDespesasFuturas buscarDespesasFuturas,
            BuscarProjecaoDespesasPorCategoria buscarProjecaoDespesasPorCategoria
    ){
        this.buscarParcelasAtivasDeCartao = buscarParcelasAtivasDeCartao;
        this.buscarDespesasRecorrenteDeCartao = buscarDespesasRecorrenteDeCartao;
        this.buscarDespesasFuturas = buscarDespesasFuturas;
        this.buscarProjecaoDespesasPorCategoria = buscarProjecaoDespesasPorCategoria;
    }

    public List<ProjecaoDespesaMensalProjection> executar(
            Long usuarioId,
            Integer mesesProjecao
    ){
        Map<YearMonth, BigDecimal> mapValorTotalMes = new TreeMap<>();
        Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes = new TreeMap<>();

        adicionarParcelasAtivasDeCartaoPorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);
        adicionarDespesasRecorrenteDeCartaoPorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);
        adicionarDespesasFuturasPorUsuario(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);
        adicionarProjecaoDespesasPorCategoria(usuarioId, mesesProjecao, mapValorTotalMes, mapDespesasMes);

        return mapperDespesaMensal(mapValorTotalMes, mapDespesasMes);
    }

    private void adicionarParcelasAtivasDeCartaoPorUsuario(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes
    ){
        buscarParcelasAtivasDeCartao.executar(usuarioId).forEach(despesa -> {
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
                        ProjecaoDespesaMensalItensProjection.builder()
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
            Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes
    ) {
        buscarDespesasRecorrenteDeCartao.executar(usuarioId).forEach(despesa -> {
            for(int proximaMes= 1; proximaMes <= mesesProjecao; proximaMes++) {
                var anoMesProjetado = despesa.getDataVencimento().plusMonths(proximaMes);

                mapValorTotalMes.merge(
                        YearMonth.from(anoMesProjetado),
                        despesa.getValor(),
                        BigDecimal::add
                );

                mapDespesasMes.computeIfAbsent(
                        YearMonth.from(anoMesProjetado),
                        despesas -> new ArrayList<>()
                ).add(
                        ProjecaoDespesaMensalItensProjection.builder()
                                .usuarioId(despesa.getUsuarioId())
                                .descricaoOriginal(despesa.getDescricaoOriginal())
                                .descricaoProcessada(despesa.getDescricaoProcessada())
                                .categoriaId(despesa.getCategoriaId())
                                .categoriaNome(despesa.getCategoriaNome())
                                .cartaoId(despesa.getCartaoId())
                                .cartaoNome(despesa.getCartaoNome())
                                .parcelaAtual(despesa.getParcelaAtual() + proximaMes)
                                .totalParcelas(despesa.getTotalParcelas())
                                .dataVencimento(despesa.getDataVencimento().plusMonths(proximaMes))
                                .observacao(despesa.getObservacao())
                                .recorrente(despesa.isRecorrente())
                                .valor(despesa.getValor())
                                .build()
                );
            }
        });
    }

    private void adicionarDespesasFuturasPorUsuario(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes
    ) {
        YearMonth anoMesAtual = YearMonth.now().minusMonths(1);
        YearMonth anoMesLimite = anoMesAtual.plusMonths(mesesProjecao).plusMonths(2);

        buscarDespesasFuturas.executar(usuarioId).forEach(despesa -> {
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

    private void adicionarProjecaoDespesasPorCategoria(
            Long usuarioId,
            Integer mesesProjecao,
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes
    ) {
        buscarProjecaoDespesasPorCategoria.executar(usuarioId).forEach(despesa -> {
            for(int proximoMes = 1; proximoMes <= mesesProjecao; proximoMes++) {
                var anoMesProjetado = despesa.getDataVencimento().plusMonths(proximoMes);

                mapValorTotalMes.merge(
                        YearMonth.from(anoMesProjetado),
                        despesa.getValor(),
                        BigDecimal::add
                );

                mapDespesasMes.computeIfAbsent(
                        YearMonth.from(anoMesProjetado),
                        despesas -> new ArrayList<>()
                ).add(
                        ProjecaoDespesaMensalItensProjection.builder()
                                .usuarioId(despesa.getUsuarioId())
                                .descricaoOriginal(despesa.getDescricaoOriginal())
                                .descricaoProcessada(despesa.getDescricaoProcessada())
                                .categoriaId(despesa.getCategoriaId())
                                .categoriaNome(despesa.getCategoriaNome())
                                .cartaoId(despesa.getCartaoId())
                                .cartaoNome(despesa.getCartaoNome())
                                .parcelaAtual(despesa.getParcelaAtual() + proximoMes)
                                .totalParcelas(despesa.getTotalParcelas())
                                .dataVencimento(despesa.getDataVencimento().plusMonths(proximoMes))
                                .observacao(despesa.getObservacao())
                                .recorrente(despesa.isRecorrente())
                                .valor(despesa.getValor())
                                .build()
                );
            }
        });
    }

    private  List<ProjecaoDespesaMensalProjection> mapperDespesaMensal(
            Map<YearMonth, BigDecimal> mapValorTotalMes,
            Map<YearMonth, List<ProjecaoDespesaMensalItensProjection>> mapDespesasMes
    ){
        return mapValorTotalMes.entrySet().stream()
                .map(entry -> {
                    var despesasDoMes = mapDespesasMes.getOrDefault(entry.getKey(), List.of());

                    BigDecimal valorTotalParcelado = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItensProjection::isParcelado)
                            .map(ProjecaoDespesaMensalItensProjection::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal valorTotalRecorrente = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItensProjection::isRecorrente)
                            .map(ProjecaoDespesaMensalItensProjection::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal valorTotalAvulso = despesasDoMes.stream()
                            .filter(ProjecaoDespesaMensalItensProjection::isAvulso)
                            .map(ProjecaoDespesaMensalItensProjection::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return ProjecaoDespesaMensalProjection.builder()
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
