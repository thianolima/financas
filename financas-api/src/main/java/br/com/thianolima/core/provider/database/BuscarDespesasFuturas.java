package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalItensProjection;

import java.util.List;

public interface BuscarDespesasFuturas {
    List<ProjecaoDespesaMensalItensProjection> executar(Long usuarioId);
}
