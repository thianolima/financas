package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {
    Long id;
    Long usuarioId;
    Long categoriaId;
    String nome;
    String palavrasChave;
}
