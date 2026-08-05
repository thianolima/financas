package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponse {
    Long id;
    String nome;
    BandeiraEnum bandeira;
    Integer diaVencimento;
    String numeroFinal;
    String titular;
    BigDecimal valorLimite;
    String cor;
    Boolean cartaoAdicional;

    public CartaoResponse(Cartao cartao) {
        this.id = cartao.getId();
        this.nome = cartao.getNome();
        this.bandeira = cartao.getBandeira();
        this.diaVencimento = cartao.getDiaVencimento();
        this.numeroFinal = cartao.getNumeroFinal();
        this.titular = cartao.getTitular();
        this.valorLimite = cartao.getValorLimite();
        this.cor = cartao.getCor();
        this.cartaoAdicional = cartao.getCartaoAdicional();
    }
}
