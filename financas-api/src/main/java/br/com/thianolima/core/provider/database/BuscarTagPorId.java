package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Tag;

import java.util.Optional;

public interface BuscarTagPorId {

    Optional<Tag> executar(Long tagId, Long usuarioId);
}

