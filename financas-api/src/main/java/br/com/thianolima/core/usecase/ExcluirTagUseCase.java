package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarDespesasPorTag;
import br.com.thianolima.core.provider.database.BuscarTagPorId;
import br.com.thianolima.core.provider.database.ExcluirTag;

public class ExcluirTagUseCase {

    private final BuscarTagPorId buscarTagPorId;
    private final ExcluirTag excluirTag;
    private final BuscarDespesasPorTag buscarDespesasPorTag;

    public ExcluirTagUseCase(
            BuscarTagPorId buscarTagPorId,
            ExcluirTag excluirTag,
            BuscarDespesasPorTag buscarDespesasPorTag
    ) {
        this.buscarTagPorId = buscarTagPorId;
        this.excluirTag = excluirTag;
        this.buscarDespesasPorTag = buscarDespesasPorTag;
    }

    public void executar(Long tagId, Long usuarioId) {
        buscarTagPorId.executar(tagId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Tag nao encontrada"));

        if (!buscarDespesasPorTag.executar(tagId, usuarioId).isEmpty()) {
            throw new RuntimeException("Nao se pode excluir uma tag com despesas vinculadas");
        }

        excluirTag.executar(tagId);
    }
}

