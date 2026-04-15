package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fatura {
    private Long id;
    private Long usuarioId;
    private Long cartaoId;
    private String anoMes;
    private Integer quantidadeDespesas;
    private String s3Bucket;
    private String s3Key;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private FaturaSituacaoEnum situacao;
}
