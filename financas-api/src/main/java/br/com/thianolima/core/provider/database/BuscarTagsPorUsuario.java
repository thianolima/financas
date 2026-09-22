package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Tag;

import java.util.List;

public interface BuscarTagsPorUsuario {

    List<Tag> executar(Long usuarioId);
}
