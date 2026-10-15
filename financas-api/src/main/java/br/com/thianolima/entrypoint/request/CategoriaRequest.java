package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Categoria;
import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(
        @NotBlank String nome,
        Boolean incluirProjecao
) {
    public CategoriaRequest {
        if (incluirProjecao == null) {
            incluirProjecao = false;
        }
    }

    public Categoria toModel() {
        return Categoria.builder()
                .nome(nome)
                .incluirProjecao(incluirProjecao)
                .build();
    }
}

