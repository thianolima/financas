package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.model.ProjecaoDespesaMensalItens;

import java.util.List;

public interface BuscarDespesasRecorrentePorUsuario {
    List<ProjecaoDespesaMensalItens> executar(Long usuarioId);
}
