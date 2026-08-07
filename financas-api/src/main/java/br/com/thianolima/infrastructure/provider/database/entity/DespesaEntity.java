package br.com.thianolima.infrastructure.provider.database.entity;


import br.com.thianolima.model.Despesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    Long categoriaId;

    @Column(name = "fatura_id")
    Long faturaId;

    @Column(name = "fornecedor_id")
    Long fornecedorId;

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
//    private List<TagEntity> tags;

    public boolean isParcelado(){
        return parcelaAtual > 0 && totalParcelas > 0;
    }

    public boolean isRecorrente(){
        return recorrente;
    }

    public boolean isAvulso(){
        return !isRecorrente() && !isParcelado();
    }

    public DespesaEntity(Despesa despesa){
        this.id = despesa.getId();
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
        this.dataVencimento = despesa.getDataVencimento();
        this.valor = despesa.getValor();
        this.observacao = despesa.getObservacao();
        this.recorrente = despesa.getRecorrente();
    }

    public Despesa toModel() {
        return Despesa.builder()
                .id(this.id)
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
                .dataVencimento(this.dataVencimento)
                .valor(this.valor)
                .observacao(this.observacao)
                .recorrente(this.recorrente)
                .build();
    }
}
