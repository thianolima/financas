package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private String descricaoOriginal;
    private String descricaoProcessada;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private Integer sequencia;
    private LocalDate dataDespesa;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private String observacao;
    private Boolean recorrente = false;
    private List<Tag> tags;

    public boolean isParcelado(){
        return this.totalParcelas > 0;
    }

    public boolean isUltimaParcela(){
        return this.totalParcelas.equals(parcelaAtual);
    }

    public boolean isPrimeiraParcela(){
        return this.parcelaAtual.equals(1);
    }

    public boolean isRecorrente(){
        return this.recorrente;
    }

    public Integer getParcelaAnterior(){
        if (isParcelado() && parcelaAtual > 1)
            return parcelaAtual - 1;
        else {
            return parcelaAtual;
        }
    }

    public Optional<LocalDate> getProximaDataVencimentoParcela(){
        if (parcelaAtual < totalParcelas)
            return Optional.of(dataVencimento.plusMonths(1));
        else {
            return Optional.empty();
        }
    }

    public boolean isOrigemFatura(){
        return this.faturaId != null;
    }
}
