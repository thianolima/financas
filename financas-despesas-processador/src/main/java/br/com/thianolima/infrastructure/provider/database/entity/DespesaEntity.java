package br.com.thianolima.infrastructure.provider.database.entity;


import br.com.thianolima.model.Despesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

    private Long usuarioId;
    private Long cartaoId;
    private Long faturaId;
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

    public DespesaEntity(Despesa despesa){
        this.usuarioId = despesa.getUsuarioId();
        this.cartaoId = despesa.getCartaoId();
        this.faturaId = despesa.getFaturaId();
        this.categoriaId = despesa.getCategoriaId();
        this.fornecedorId = despesa.getFornecedorId();
        this.descricaoOriginal = despesa.getDescricaoOriginal();
        this.descricaoProcessada = despesa.getDescricaoProcessada();
        this.parcelaAtual = despesa.getParcelaAtual();
        this.totalParcelas = despesa.getTotalParcelas();
        this.sequencia = despesa.getSequencia();
        this.dataDespesa = despesa.getDataDespesa();
        this.valor = despesa.getValor();
        this.observacao = despesa.getObservacao();
        this.recorrente = despesa.getRecorrente();
    }

    public Despesa toModel() {
        return Despesa.builder()
                .usuarioId(this.usuarioId)
                .cartaoId(this.cartaoId)
                .faturaId(this.faturaId)
                .categoriaId(this.categoriaId)
                .fornecedorId(this.fornecedorId)
                .descricaoOriginal(this.descricaoOriginal)
                .descricaoProcessada(this.descricaoProcessada)
                .parcelaAtual(this.parcelaAtual)
                .totalParcelas(this.totalParcelas)
                .sequencia(this.sequencia)
                .dataDespesa(this.dataDespesa)
                .valor(this.valor)
                .observacao(this.observacao)
                .recorrente(this.recorrente)
                .build();
    }
}
