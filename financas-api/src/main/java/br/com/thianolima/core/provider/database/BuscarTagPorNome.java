package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Tag;

import java.util.Optional;

public interface BuscarTagPorNome {

    Optional<Tag> executar(String nome, Long usuarioId);
}
