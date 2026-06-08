package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.DespesaPaginada;
import br.com.thianolima.core.model.DespesaPaginadaItem;
import br.com.thianolima.core.provider.database.BuscarDespesasPorUsuario;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class BuscarDespesasPorUsuarioUseCase {

    private final BuscarDespesasPorUsuario buscarDespesasPorUsuario;

    public BuscarDespesasPorUsuarioUseCase(
            BuscarDespesasPorUsuario buscarDespesasPorUsuario
    ) {
        this.buscarDespesasPorUsuario = buscarDespesasPorUsuario;
    }

    public DespesaPaginada executar(
            Long usuarioId,
            YearMonth anoMes,
            Integer pagina,
            Integer tamanho
    ){
        var despesas = buscarDespesasPorUsuario.executar(usuarioId, anoMes);
        var valorTotal = calcularValorTotal(despesas);
        var valorTotalParcelado = calcularValorTotalParcelado(despesas);
        var valorTotalRecorrente = calcularValorTotalRcorrente(despesas);
        var valorTotalAvulso = calcularValorTotalAvulso(despesas);

        var totalRegistros = despesas.size();
        var totalPaginas = Math.abs(totalRegistros / tamanho);
        var paginaAtual = Math.min(pagina,totalPaginas);
        var inicio = paginaAtual * tamanho;
        var fim = inicio + tamanho;
        var despesasPaginada = despesas.subList(inicio, Math.min(fim, despesas.size()));

        return DespesaPaginada.builder()
                .totalRegistros(totalRegistros)
                .paginaAtual(paginaAtual)
                .totalPaginas(totalPaginas)
                .registrosPorPagina(tamanho)
                .valorTotal(valorTotal)
                .valorTotalParcelado(valorTotalParcelado)
                .valorTotalRecorrente(valorTotalRecorrente)
                .valorTotalAvulso(valorTotalAvulso)
                .despesas(despesasPaginada)
                .build();
    }

    private BigDecimal calcularValorTotal(List<DespesaPaginadaItem> despesas){
        return despesas.stream()
                .map(DespesaPaginadaItem::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalParcelado(List<DespesaPaginadaItem> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItem::isParcelado)
                .map(DespesaPaginadaItem::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalRcorrente(List<DespesaPaginadaItem> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItem::isRecorrente)
                .map(DespesaPaginadaItem::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalAvulso(List<DespesaPaginadaItem> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItem::isAvulso)
                .map(DespesaPaginadaItem::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
