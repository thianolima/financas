package br.com.thianolima.core.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesaPaginadaProjection {
    private Integer paginaAtual;
    private Integer totalPaginas;
    private Integer totalRegistros;
    private Integer registrosPorPagina;
    private BigDecimal valorTotal;
    private BigDecimal valorTotalParcelado;
    private BigDecimal valorTotalRecorrente;
    private BigDecimal valorTotalAvulso;
    private List<DespesaPaginadaItemProjection> despesas;
}