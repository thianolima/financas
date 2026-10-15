package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorId;
import br.com.thianolima.core.provider.database.BuscarCategoriaPorNome;
import br.com.thianolima.core.provider.database.SalvarCategoria;
import br.com.thianolima.model.Categoria;

public class AlterarCategoriaUseCase {

    private final SalvarCategoria salvarCategoria;
    private final BuscarCategoriaPorNome buscarCategoriaPorNome;
    private final BuscarCategoriaPorId buscarCategoriaPorId;

    public AlterarCategoriaUseCase(
            SalvarCategoria salvarCategoria,
            BuscarCategoriaPorNome buscarCategoriaPorNome,
            BuscarCategoriaPorId buscarCategoriaPorId
    ) {
        this.salvarCategoria = salvarCategoria;
        this.buscarCategoriaPorNome = buscarCategoriaPorNome;
        this.buscarCategoriaPorId = buscarCategoriaPorId;
    }

    public void executar(Categoria categoria) {
        var categoriaSalva = buscarCategoriaPorId.executar(categoria.getId(), categoria.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));

        if (!categoriaSalva.getUsuarioId().equals(categoria.getUsuarioId())) {
            throw new RuntimeException("Categoria nao encontrada");
        }

        buscarCategoriaPorNome.executar(categoria.getNome(), categoria.getUsuarioId())
                .ifPresent(categoriaMesmoNome -> {
                    if (!categoriaMesmoNome.getId().equals(categoria.getId())) {
                        throw new RuntimeException("Nome de categoria ja cadastrado");
                    }
                });

        salvarCategoria.executar(categoria);
    }
}

