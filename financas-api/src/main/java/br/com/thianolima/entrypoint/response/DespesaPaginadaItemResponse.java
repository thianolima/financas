package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.model.DespesaPaginadaItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DespesaPaginadaItemResponse {
    private Long id;
    private Long cartaoId;
    private String cartaoNome;
    private String cartaoCor;
    private Long categoriaId;
    private String categoriaNome;
    private String descricao;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private LocalDate dataDespesa;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private String observacao;
    Boolean recorrente;
    Boolean avulso;
    Boolean parcelado;

    public DespesaPaginadaItemResponse(DespesaPaginadaItem despesaPaginadaItem){
        this.id = despesaPaginadaItem.getId();
        this.cartaoId = despesaPaginadaItem.getCartaoId();
        this.cartaoNome = despesaPaginadaItem.getCartaoNome();
        this.cartaoCor = despesaPaginadaItem.getCartaoCor();
        this.categoriaId = despesaPaginadaItem.getCategoriaId();
        this.categoriaNome = despesaPaginadaItem.getCategoriaNome();
        this.descricao = despesaPaginadaItem.getDescricao();
        this.parcelaAtual = despesaPaginadaItem.getParcelaAtual();
        this.totalParcelas = despesaPaginadaItem.getTotalParcelas();
        this.dataDespesa = despesaPaginadaItem.getDataDespesa();
        this.dataVencimento = despesaPaginadaItem.getDataVencimento();
        this.valor = despesaPaginadaItem.getValor();
        this.observacao = despesaPaginadaItem.getObservacao();
        this.recorrente = despesaPaginadaItem.isRecorrente();
        this.avulso = despesaPaginadaItem.isAvulso();
        this.parcelado = despesaPaginadaItem.isParcelado();
    }
}
