package br.com.thianolima.model;

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
public class Despesa {
    private Long id;
    private Long faturaId;
    private Long usuarioId;
    private Long cartaoId;
    private Long categoriaId;
    private Long fornecedorId;
    private String descricaoOriginal;
    private String descricaoProcessada;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private Integer sequencia;
    private LocalDate dataDespesa;
    private BigDecimal valor;
    private String observacao;
    private Boolean recorrente = false;

    public boolean isParcelado(){
        return this.totalParcelas > 0;
    }

    public boolean isUltimaParcela(){
        return this.totalParcelas.equals(parcelaAtual);
    }

    public boolean isPrimeiraParcela(){
        return this.parcelaAtual.equals(1);
    }

    public boolean isRecorrete(){
        return this.recorrente;
    }

    public Integer getParcelaAnterior(){
        if (isParcelado() && parcelaAtual > 1)
            return parcelaAtual - 1;
        else
            return parcelaAtual;
    }
}
