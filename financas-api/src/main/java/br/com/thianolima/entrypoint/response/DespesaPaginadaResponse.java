package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.model.DespesaPaginada;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class DespesaPaginadaResponse {
    private Integer paginaAtual;
    private Integer totalPaginas;
    private Integer totalRegistros;
    private Integer registrosPorPagina;
    private BigDecimal valorTotal;
    private BigDecimal valorTotalParcelado;
    private BigDecimal valorTotalRecorrente;
    private BigDecimal valorTotalAvulso;
    private List<DespesaPaginadaItemResponse> despesas;

    public DespesaPaginadaResponse(DespesaPaginada despesaPaginada){
        this.paginaAtual = despesaPaginada.getPaginaAtual();
        this.totalPaginas = despesaPaginada.getTotalPaginas();
        this.totalRegistros = despesaPaginada.getTotalRegistros();
        this.registrosPorPagina = despesaPaginada.getRegistrosPorPagina();
        this.valorTotal = despesaPaginada.getValorTotal();
        this.valorTotalParcelado = despesaPaginada.getValorTotalParcelado();
        this.valorTotalRecorrente = despesaPaginada.getValorTotalRecorrente();
        this.valorTotalAvulso = despesaPaginada.getValorTotalAvulso();
        this.despesas = despesaPaginada.getDespesas().stream()
                .map(DespesaPaginadaItemResponse::new)
                .toList();
    }
}
