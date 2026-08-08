package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.TipoDespesaEnum;
import br.com.thianolima.core.projection.DespesaPaginadaItemProjection;
import br.com.thianolima.core.projection.DespesaPaginadaProjection;
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

    public DespesaPaginadaProjection executar(
            Long usuarioId,
            YearMonth anoMes,
            Integer pagina,
            Integer tamanho,
            Long cartaoId,
            Long categoriaId,
            TipoDespesaEnum tipo
    ){
        var despesas = buscarDespesasPorUsuario.executar(
                usuarioId,
                anoMes,
                cartaoId,
                categoriaId,
                tipo
        );
        var valorTotal = calcularValorTotal(despesas);
        var valorTotalParcelado = calcularValorTotalParcelado(despesas);
        var valorTotalRecorrente = calcularValorTotalRcorrente(despesas);
        var valorTotalAvulso = calcularValorTotalAvulso(despesas);

        var totalRegistros = despesas.size();
        var totalPaginas = ((totalRegistros % tamanho) > 0) ? (totalRegistros / tamanho) + 1 : totalRegistros / tamanho;
        var paginaAtual = Math.min(pagina,totalPaginas);
        var inicio = paginaAtual * tamanho;
        var fim = inicio + tamanho;
        var despesasPaginada = despesas.subList(inicio, Math.min(fim, despesas.size()));

        return DespesaPaginadaProjection.builder()
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

    private BigDecimal calcularValorTotal(List<DespesaPaginadaItemProjection> despesas){
        return despesas.stream()
                .map(DespesaPaginadaItemProjection::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalParcelado(List<DespesaPaginadaItemProjection> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItemProjection::isParcelado)
                .map(DespesaPaginadaItemProjection::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalRcorrente(List<DespesaPaginadaItemProjection> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItemProjection::isRecorrente)
                .map(DespesaPaginadaItemProjection::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorTotalAvulso(List<DespesaPaginadaItemProjection> despesas){
        return despesas.stream()
                .filter(DespesaPaginadaItemProjection::isAvulso)
                .map(DespesaPaginadaItemProjection::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
