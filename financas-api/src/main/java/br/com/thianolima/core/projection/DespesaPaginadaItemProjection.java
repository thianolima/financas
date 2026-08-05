package br.com.thianolima.core.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesaPaginadaItemProjection {
    private Long id;
    private Long cartaoId;
    private String cartaoNome;
    private Long categoriaId;
    private String cartaoCor;
    private String categoriaNome;
    private String descricao;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private LocalDate dataDespesa;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private String observacao;
    Boolean recorrente;

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