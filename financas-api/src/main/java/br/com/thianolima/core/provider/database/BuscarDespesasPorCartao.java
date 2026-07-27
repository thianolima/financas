package br.com.thianolima.core.provider.database;

import br.com.thianolima.model.Despesa;

import java.util.List;

public interface BuscarDespesasPorCartao {

     List<Despesa> executar(Long cartaoId);
}
