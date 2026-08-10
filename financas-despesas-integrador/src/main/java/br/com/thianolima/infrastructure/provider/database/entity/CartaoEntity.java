package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cartoes")
public class CartaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartao_id")
    Long id;

    Long usuarioId;

    String nome;

    @Enumerated(EnumType.STRING)
    BandeiraEnum bandeira;

    @Column(name = "dia_vencimento")
    Integer diaVencimento;

    @Column(name = "numero_final")
    String numeroFinal;

    String titular;

    @Column(name = "valor_limite")
    BigDecimal valorLimite;

    String cor;

    @Column(name = "cartao_adicional")
    Boolean cartaoAdicional = false;

    public Cartao toModel(){
        return Cartao.builder()
                .id(this.id)
                .bandeira(this.bandeira)
                .usuarioId(this.usuarioId)
                .nome(this.nome)
                .diaVencimento(this.diaVencimento)
                .numeroFinal(this.numeroFinal)
                .titular(this.titular)
                .valorLimite(this.valorLimite)
                .cor(this.cor)
                .cartaoAdicional(this.cartaoAdicional)
                .build();
    }
}


