package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCategoriasPorUsuario;
import br.com.thianolima.model.Categoria;

import java.util.List;

public class BuscarCategoriasPorUsuarioUseCase {

    private final BuscarCategoriasPorUsuario buscarCategoriasPorUsuario;

    public BuscarCategoriasPorUsuarioUseCase(
            BuscarCategoriasPorUsuario buscarCategoriasPorUsuario
    ) {
        this.buscarCategoriasPorUsuario = buscarCategoriasPorUsuario;
    }

    public List<Categoria> executar(Long usuarioId) {
        return buscarCategoriasPorUsuario.executar(usuarioId);
    }
}
