package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.FaturaSituacaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private FaturaSituacaoEnum situacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_id")
    CartaoEntity cartao;

}
