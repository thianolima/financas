package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarTagPorId;
import br.com.thianolima.core.provider.database.BuscarTagPorNome;
import br.com.thianolima.core.provider.database.SalvarTag;
import br.com.thianolima.model.Tag;

public class AlterarTagUseCase {
    private final SalvarTag salvarTag;
    private final BuscarTagPorNome buscarTagPorNome;
    private final BuscarTagPorId buscarTagPorId;

    public AlterarTagUseCase(
            SalvarTag salvarTag,
            BuscarTagPorNome buscarTagPorNome,
            BuscarTagPorId buscarTagPorId
    ) {
        this.salvarTag = salvarTag;
        this.buscarTagPorNome = buscarTagPorNome;
        this.buscarTagPorId = buscarTagPorId;
    }

    public void executar(Tag tag) {
        buscarTagPorId.executar(tag.getId(), tag.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Tag nao encontrada"));

        buscarTagPorNome.executar(tag.getNome(), tag.getUsuarioId()).ifPresent(tagSalva -> {
            if(!tagSalva.getId().equals(tag.getId())) {
                throw new RuntimeException("Nome de Tag já cadastrado");
            }
        });
        salvarTag.executar(tag);
    }
}
