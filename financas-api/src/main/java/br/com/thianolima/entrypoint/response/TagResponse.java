package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.Tag;

public record TagResponse (
        Long id,
        String nome
){
    public TagResponse(Tag tag){
        this(
                tag.getId(),
                tag.getNome()
        );
    }
}