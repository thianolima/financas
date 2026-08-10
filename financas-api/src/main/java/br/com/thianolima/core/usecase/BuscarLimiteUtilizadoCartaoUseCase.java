package br.com.thianolima.core.usecase;

import br.com.thianolima.core.projection.CartaoLimiteProjection;
import br.com.thianolima.core.provider.database.BuscarLimiteUtilizadoCartao;

import java.util.List;

public class BuscarLimiteUtilizadoCartaoUseCase {

    private final BuscarLimiteUtilizadoCartao buscarLimiteUtilizadoCartao;

    public BuscarLimiteUtilizadoCartaoUseCase(
            BuscarLimiteUtilizadoCartao buscarLimiteUtilizadoCartao
    ) {
        this.buscarLimiteUtilizadoCartao = buscarLimiteUtilizadoCartao;
    }

    public List<CartaoLimiteProjection> executar(Long cartaoId, Long usuarioId) {
        return buscarLimiteUtilizadoCartao.executar(cartaoId, usuarioId);
    }
}
