package br.com.thianolima.core.projection;

import java.math.BigDecimal;
import java.util.List;

public record DespesaPaginadaProjection(
        Integer paginaAtual,
        Integer totalPaginas,
        Integer totalRegistros,
        Integer registrosPorPagina,
        BigDecimal valorTotal,
        BigDecimal valorTotalParcelado,
        BigDecimal valorTotalRecorrente,
        BigDecimal valorTotalAvulso,
        List<DespesaPaginadaItemProjection> despesas
) {}
