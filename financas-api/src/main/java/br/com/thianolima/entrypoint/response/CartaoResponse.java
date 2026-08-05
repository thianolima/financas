package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;

import java.math.BigDecimal;

public record CartaoResponse(
        Long id,
        String nome,
        BandeiraEnum bandeira,
        Integer diaVencimento,
        String numeroFinal,
        String titular,
        BigDecimal valorLimite,
        String cor,
        Boolean cartaoAdicional
) {
    public CartaoResponse(Cartao cartao) {
        this(
                cartao.getId(),
                cartao.getNome(),
                cartao.getBandeira(),
                cartao.getDiaVencimento(),
                cartao.getNumeroFinal(),
                cartao.getTitular(),
                cartao.getValorLimite(),
                cartao.getCor(),
                cartao.getCartaoAdicional()
        );
    }
}
