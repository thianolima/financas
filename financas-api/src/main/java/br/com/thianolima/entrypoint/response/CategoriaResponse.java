package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.Categoria;

public record CategoriaResponse(
        Long id,
        String nome,
        Boolean incluirProjecao
) {

    public CategoriaResponse(Categoria categoria) {
        this(
                categoria.getId(),
                categoria.getNome(),
                categoria.getIncluirProjecao()
        );
    }
}
