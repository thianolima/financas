package br.com.thianolima.core.projection;

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
    private BigDecimal valorLimite;
    private BigDecimal valorLimiteUtilizado;
}
