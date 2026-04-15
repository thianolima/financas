package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Cartao;
import br.com.thianolima.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface BuscarCartaoPorUsuario {

    Optional<List<Cartao>> executar(Usuario usuario);
}
