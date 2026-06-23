package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_regras")
@Builder
public class RegraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regra_id")
    Long id;

    @Column(name = "usuario_id")
    Long usuarioId;

    @Column(name = "categoria_id")
    Long categoriaId;

    String descricao;

    @OneToMany(
            mappedBy = "regra",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    List<RegraTermoEntity> termos;

    public RegraEntity(Regra regra) {
        this.id = regra.getId();
        this.usuarioId = regra.getUsuarioId();
        this.categoriaId = regra.getCategoriaId();
        this.descricao = regra.getDescricao();
        if (regra.getTermos() != null) {
            this.termos = regra.getTermos().stream().map(termo -> {
                RegraTermoEntity termoEntity = new RegraTermoEntity(termo);
                termoEntity.setRegra(this);
                return termoEntity;
            }).collect(Collectors.toList());
        }
    }

    public Regra toModel() {
        return Regra.builder()
                .id(this.id)
                .usuarioId(this.usuarioId)
                .categoriaId(this.categoriaId)
                .descricao(this.descricao)
                .termos(
                    this.termos.stream().map( termo ->
                        RegraTermo.builder()
                                .id(termo.getId())
                                .regraId(termo.getRegra().getId())
                                .termoBusca(termo.getTermoBusca())
                                .build()
                    ).collect(Collectors.toList())
                )
                .build();
    }
}
