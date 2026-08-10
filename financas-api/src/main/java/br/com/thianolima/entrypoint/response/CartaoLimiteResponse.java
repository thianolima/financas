package br.com.thianolima.entrypoint.response;

import java.math.BigDecimal;

public record CartaoLimiteResponse(
        Long cartaoId,
        BigDecimal valorLimite,
        BigDecimal valorLimiteUtilizado
) {
    public CartaoLimiteResponse(Long cartaoId, BigDecimal valorLimite, BigDecimal valorLimiteUtilizado) {
        this.cartaoId = cartaoId;
        this.valorLimite = valorLimite;
        this.valorLimiteUtilizado = valorLimiteUtilizado;
    }}
