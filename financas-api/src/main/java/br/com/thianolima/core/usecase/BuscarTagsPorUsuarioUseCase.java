package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarTagsPorUsuarioId;
import br.com.thianolima.model.Tag;

import java.util.List;

public class BuscarTagsPorUsuarioUseCase {

    private final BuscarTagsPorUsuarioId buscarTagsPorUsuarioId;

    public BuscarTagsPorUsuarioUseCase(
            BuscarTagsPorUsuarioId buscarTagsPorUsuarioId
    ) {
        this.buscarTagsPorUsuarioId = buscarTagsPorUsuarioId;
    }

    public List<Tag> executar(Long usuarioId){
        return buscarTagsPorUsuarioId.executar(usuarioId);
    }
}
