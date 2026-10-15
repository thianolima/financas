package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Despesa;

import java.util.List;

public interface BuscarDespesasPorTag {

    List<Despesa> executar(Long tagId, Long usuarioId);
}

