package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Categoria;

import java.util.Optional;

public interface BuscarCategoriaPorNome {

    Optional<Categoria> executar(String nome, Long usuarioId);
}

