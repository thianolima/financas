package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cartao {
    Long id;
    BandeiraEnum bandeira;
    Long usuarioId;
    String nome;
    Integer diaVencimento;
    String numeroFinal;
    String titular;
    BigDecimal valorLimite;
    String cor;
    Boolean cartaoAdicional = false;

}
