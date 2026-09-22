package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarTagsPorUsuario;
import br.com.thianolima.model.Tag;

import java.util.List;

public class BuscarTagsPorUsuarioUseCase {

    private final BuscarTagsPorUsuario buscarTagsPorUsuario;

    public BuscarTagsPorUsuarioUseCase(
            BuscarTagsPorUsuario buscarTagsPorUsuario
    ) {
        this.buscarTagsPorUsuario = buscarTagsPorUsuario;
    }

    public List<Tag> executar(Long usuarioId){
        return buscarTagsPorUsuario.executar(usuarioId);
    }
}
