package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.model.ProjecaoDespesaMensalItens;

import java.util.List;

public interface BuscarProjecaoDespesasPorCategoria {

    List<ProjecaoDespesaMensalItens> executar(Long usuarioId);
}
