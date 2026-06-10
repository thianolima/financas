package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
