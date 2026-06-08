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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_id")
    CartaoEntity cartao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    CategoriaEntity categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fatura_id")
    FaturaEntity fatura;

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
