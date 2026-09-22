package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarRegrasPorUsuario;
import br.com.thianolima.model.Regra;

import java.util.List;

public class BuscarRegrasPorUsuarioUseCase {

    private final BuscarRegrasPorUsuario buscarRegrasPorUsuario;

    public BuscarRegrasPorUsuarioUseCase(
            BuscarRegrasPorUsuario buscarRegrasPorUsuario
    ) {
        this.buscarRegrasPorUsuario = buscarRegrasPorUsuario;
    }

    public List<Regra> executar (Long usuarioId) {
        return buscarRegrasPorUsuario.executar(usuarioId);
    }
}
