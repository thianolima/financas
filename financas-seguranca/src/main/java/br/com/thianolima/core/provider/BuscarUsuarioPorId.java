package br.com.thianolima.core.provider;

import br.com.thianolima.model.Usuario;

import java.util.Optional;

public interface BuscarUsuarioPorId {

    Optional<Usuario> executar(Long idUsuario);
}
