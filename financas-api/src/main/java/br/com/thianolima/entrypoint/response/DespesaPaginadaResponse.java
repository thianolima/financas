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
                despesaPaginadaProjection.getPaginaAtual(),
                despesaPaginadaProjection.getTotalPaginas(),
                despesaPaginadaProjection.getTotalRegistros(),
                despesaPaginadaProjection.getRegistrosPorPagina(),
                despesaPaginadaProjection.getValorTotal(),
                despesaPaginadaProjection.getValorTotalParcelado(),
                despesaPaginadaProjection.getValorTotalRecorrente(),
                despesaPaginadaProjection.getValorTotalAvulso(),
                despesaPaginadaProjection.getDespesas().stream().map(DespesaPaginadaItemResponse::new).toList()
        );
    }
}
