package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Despesa;

import java.util.List;

public interface BuscarDespesasRecorrenteDeCartaoPorUsuario {

    List<Despesa> executar(Long usuarioId);
}
