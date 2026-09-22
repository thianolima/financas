package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Regra;

import java.util.List;

public interface BuscarRegrasPorUsuario {

    List<Regra> executar(Long usuarioId);
}
