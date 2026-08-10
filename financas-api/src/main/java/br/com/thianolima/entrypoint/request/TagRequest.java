package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Tag;
import jakarta.validation.constraints.NotNull;

public record TagRequest (
        @NotNull
        String nome
){
    public Tag toModel() {
        return Tag.builder()
                .nome(nome)
                .build();
    }
}

