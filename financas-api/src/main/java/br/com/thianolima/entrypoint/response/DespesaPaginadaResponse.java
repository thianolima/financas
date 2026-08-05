package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.projection.DespesaPaginadaProjection;

import java.math.BigDecimal;
import java.util.List;

public record DespesaPaginadaResponse(
        Integer paginaAtual,
        Integer totalPaginas,
        Integer totalRegistros,
        Integer registrosPorPagina,
        BigDecimal valorTotal,
        BigDecimal valorTotalParcelado,
        BigDecimal valorTotalRecorrente,
        BigDecimal valorTotalAvulso,
        List<DespesaPaginadaItemResponse> despesas
) {

    public DespesaPaginadaResponse(DespesaPaginadaProjection despesaPaginadaProjection){
        this(
                despesaPaginadaProjection.paginaAtual(),
                despesaPaginadaProjection.totalPaginas(),
                despesaPaginadaProjection.totalRegistros(),
                despesaPaginadaProjection.registrosPorPagina(),
                despesaPaginadaProjection.valorTotal(),
                despesaPaginadaProjection.valorTotalParcelado(),
                despesaPaginadaProjection.valorTotalRecorrente(),
                despesaPaginadaProjection.valorTotalAvulso(),
                despesaPaginadaProjection.despesas().stream().map(DespesaPaginadaItemResponse::new).toList()
        );
    }
}
