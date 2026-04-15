package br.com.thianolima.core.provider;

import br.com.thianolima.model.Fornecedor;

import java.util.List;

public interface BuscarFornecedoresPorUsuarioId {

    List<Fornecedor> executar(Long usuarioId);
}
