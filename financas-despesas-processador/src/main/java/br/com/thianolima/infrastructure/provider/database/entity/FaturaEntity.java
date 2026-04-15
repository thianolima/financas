package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Fatura;
import br.com.thianolima.model.FaturaSituacaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_faturas")
@Builder
public class FaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fatura_id")
    private Long id;

    private String anoMes;

    private Integer quantidadeDespesas;

    @Column(name = "s3_bucket")
    private String s3Bucket;

    @Column(name = "s3_key")
    private String s3Key;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataConclusao;

    @Enumerated(EnumType.STRING)
    private FaturaSituacaoEnum situacao;

    private Long usuarioId;

    private Long cartaoId;

    public Fatura toModel() {
        return Fatura.builder()
                .id(this.id)
                .anoMes(this.anoMes)
                .quantidadeDespesas(this.quantidadeDespesas)
                .s3Bucket(this.s3Bucket)
                .s3Key(this.s3Key)
                .dataCriacao(this.dataCriacao)
                .dataConclusao(this.dataConclusao)
                .situacao(this.situacao)
                .cartaoId(this.cartaoId)
                .usuarioId(this.usuarioId)
                .build();
    }
}
