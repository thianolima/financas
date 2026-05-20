package br.com.thianolima.core.provider;

import br.com.thianolima.model.Cartao;

import java.util.Optional;

public interface BuscarCartaoPorId {

    Optional<Cartao> executar(Long id);
}
