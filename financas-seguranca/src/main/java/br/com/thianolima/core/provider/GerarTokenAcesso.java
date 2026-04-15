package br.com.thianolima.core.provider;

import br.com.thianolima.model.Usuario;

public interface GerarTokenAcesso {

    String executar(Usuario usuario);
}
