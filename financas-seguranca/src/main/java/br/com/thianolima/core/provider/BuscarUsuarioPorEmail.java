package br.com.thianolima.core.provider;

import br.com.thianolima.model.Usuario;

import java.util.Optional;

public interface BuscarUsuarioPorEmail {

    Optional<Usuario> execute(String email);
}
