package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    String nome;

    @Enumerated(EnumType.STRING)
    BandeiraEnum bandeira;

    @Column(name = "dia_vencimento")
    Integer diaVencimento;

    public Cartao toModel(){
        return Cartao.builder()
                .id(this.id)
                .bandeira(this.bandeira)
                .nome(this.nome)
                .build();
    }
}


