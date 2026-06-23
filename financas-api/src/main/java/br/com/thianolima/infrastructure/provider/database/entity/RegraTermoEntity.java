package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_regras_termos")
@Builder
public class RegraTermoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "termo_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regra_id", nullable = false)
    RegraEntity regra;

    String termoBusca;

    public RegraTermoEntity(RegraTermo termo) {
            this.id = termo.getId();
            this.regra = RegraEntity.builder().id(termo.getRegraId()).build();
            this.termoBusca = termo.getTermoBusca();
    }
}
