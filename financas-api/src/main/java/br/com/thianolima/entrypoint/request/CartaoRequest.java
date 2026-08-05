package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoRequest {
    @NotEmpty
    String nome;

    BandeiraEnum bandeira;

    Integer diaVencimento;

    @NotEmpty
    String numeroFinal;

    String titular;

    @NotNull
    BigDecimal valorLimite;

    @NotNull
    String cor;

    @NotNull
    Boolean cartaoAdicional = false;

    public Cartao toModel(){
        return Cartao.builder()
                .nome(this.nome)
                .bandeira(this.bandeira)
                .numeroFinal(this.numeroFinal)
                .diaVencimento(this.diaVencimento)
                .titular(this.titular)
                .valorLimite(this.valorLimite)
                .cor(this.cor)
                .cartaoAdicional(this.cartaoAdicional)
                .build();
    }
}
