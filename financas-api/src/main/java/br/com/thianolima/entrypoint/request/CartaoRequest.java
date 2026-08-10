package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record CartaoRequest(
        @NotEmpty String nome,
        BandeiraEnum bandeira,
        Integer diaVencimento,
        @NotEmpty String numeroFinal,
        String titular,
        @NotNull BigDecimal valorLimite,
        @NotNull String cor,
        @NotNull Boolean cartaoAdicional
) {
    public CartaoRequest {
        if (cartaoAdicional == null) {
            cartaoAdicional = false;
        }
    }

    public Cartao toModel() {
        return Cartao.builder()
                .nome(nome)
                .bandeira(bandeira)
                .numeroFinal(numeroFinal)
                .diaVencimento(diaVencimento)
                .titular(titular)
                .valorLimite(valorLimite)
                .cor(cor)
                .cartaoAdicional(cartaoAdicional)
                .build();
    }
}
