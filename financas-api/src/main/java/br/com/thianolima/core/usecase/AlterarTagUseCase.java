package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarTagPorNome;
import br.com.thianolima.core.provider.database.SalvarTag;
import br.com.thianolima.model.Tag;

public class AlterarTagUseCase {
    private final SalvarTag salvarTag;
    private final BuscarTagPorNome buscarTagPorNome;

    public AlterarTagUseCase(SalvarTag salvarTag, BuscarTagPorNome buscarTagPorNome) {
        this.salvarTag = salvarTag;
        this.buscarTagPorNome = buscarTagPorNome;
    }

    public void executar(Tag tag) {
        buscarTagPorNome.executar(tag.getNome(), tag.getUsuarioId()).ifPresent(tagSalva -> {
            if(!tagSalva.getId().equals(tag.getId())) {
                throw new RuntimeException("Nome de Tag já cadastrado");
            }
        });
        salvarTag.executar(tag);
    }
}


