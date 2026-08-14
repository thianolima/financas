package br.com.thianolima.core.projection;

import br.com.thianolima.model.BandeiraEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartaoLimiteProjection {
    private Long cartaoId;
    private String nome;
    private BandeiraEnum bandeira;
    private String numeroFinal;
    private String titular;
    private String cor;
    private BigDecimal valorLimite;
    private BigDecimal valorLimiteUtilizado;
}
