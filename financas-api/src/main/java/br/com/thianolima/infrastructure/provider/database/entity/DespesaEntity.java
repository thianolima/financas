package br.com.thianolima.infrastructure.provider.database.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_despesas")
@Builder
public class DespesaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "despesa_id")
    private Long id;

    @Column(name = "usuario_id")
    Long usuarioId;

    @Column(name = "cartao_id")
    Long cartaoId;

    @Column(name = "categoria_id")
    Long cartegoriaId;

    @Column(name = "fatura_id")
    Long faturaId;

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
