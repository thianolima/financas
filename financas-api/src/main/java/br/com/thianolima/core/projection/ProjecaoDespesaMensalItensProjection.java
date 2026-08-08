package br.com.thianolima.core.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjecaoDespesaMensalItensProjection {
    private Long id;
    private Long faturaId;
    private Long usuarioId;
    private Long cartaoId;
    private String cartaoNome;
    private Long categoriaId;
    private String categoriaNome;
    private String descricaoOriginal;
    private String descricaoProcessada;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private Integer sequencia;
    private LocalDate dataDespesa;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private String observacao;
    private Boolean recorrente;

    public boolean isParcelado(){
        return parcelaAtual > 0 && totalParcelas > 0;
    }

    public boolean isRecorrente(){
        return recorrente;
    }

    public boolean isAvulso(){
        return !isRecorrente() && !isParcelado();
    }
}