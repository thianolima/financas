package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Categoria;

import java.util.List;

public interface BuscarCategoriasPorUsuario {

    List<Categoria> executar(Long usuarioId);

}
