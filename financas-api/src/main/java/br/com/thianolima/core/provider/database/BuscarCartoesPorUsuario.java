package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Cartao;

import java.util.List;

public interface BuscarCartoesPorUsuario {

    List<Cartao> executar(Long usuarioId);
}
