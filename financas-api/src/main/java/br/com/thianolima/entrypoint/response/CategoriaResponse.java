package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {
    Long id;
    String nome;
    Boolean incluirProjecao;

    public CategoriaResponse(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.incluirProjecao = categoria.getIncluirProjecao();
    }
}
