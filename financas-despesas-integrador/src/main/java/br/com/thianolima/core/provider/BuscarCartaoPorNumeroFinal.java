package br.com.thianolima.core.provider;

import br.com.thianolima.model.Cartao;

import java.util.Optional;

public interface BuscarCartaoPorNumeroFinal {

    Optional<Cartao> executar(String numeroFinal, Long usuarioId);
}
