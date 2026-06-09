package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartoesPorUsuario;
import br.com.thianolima.model.Cartao;

import java.util.List;

public class BuscarCartoesPorUsuarioUseCase {

    private final BuscarCartoesPorUsuario buscarCartoesPorUsuario;

    public BuscarCartoesPorUsuarioUseCase(BuscarCartoesPorUsuario buscarCartoesPorUsuario) {
        this.buscarCartoesPorUsuario = buscarCartoesPorUsuario;
    }

    public List<Cartao> executar(Long usuarioId) {
        return buscarCartoesPorUsuario.executar(usuarioId);
    }
}
