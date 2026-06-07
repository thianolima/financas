package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.BandeiraEnum;
import br.com.thianolima.model.Cartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponse {
    Long id;
    String nome;
    BandeiraEnum bandeira;
    Integer diaVencimento;

    public CartaoResponse(Cartao cartao) {
        this.id = cartao.getId();
        this.nome = cartao.getNome();
        this.bandeira = cartao.getBandeira();
        this.diaVencimento = cartao.getDiaVencimento();
    }
}
