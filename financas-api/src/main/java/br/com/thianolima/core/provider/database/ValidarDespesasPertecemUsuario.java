package br.com.thianolima.core.provider.database;

import java.util.List;

public interface ValidarDespesasPertecemUsuario {

    Boolean executar(List<Long> despesasIds, Long usuarioId);
}
