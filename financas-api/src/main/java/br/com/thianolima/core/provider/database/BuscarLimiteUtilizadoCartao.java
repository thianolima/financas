package br.com.thianolima.core.provider.database;

import br.com.thianolima.core.projection.CartaoLimiteProjection;

import java.util.List;

public interface BuscarLimiteUtilizadoCartao {
    List<CartaoLimiteProjection> executar(Long cartaoId, Long usuarioId);
}
