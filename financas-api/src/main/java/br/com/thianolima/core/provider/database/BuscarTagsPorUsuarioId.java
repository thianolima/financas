package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Tag;

import java.util.List;

public interface BuscarTagsPorUsuarioId {

    List<Tag> executar(Long usuarioId);
}
