package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Categoria;

import java.util.Optional;

public interface BuscarCategoriaPorId {

    Optional<Categoria> executar(Long categoriaId, Long usuarioId);
}
