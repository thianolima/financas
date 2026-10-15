package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorNome;
import br.com.thianolima.core.provider.database.SalvarCategoria;
import br.com.thianolima.model.Categoria;

public class InserirCategoriaUseCase {

    private final SalvarCategoria salvarCategoria;
    private final BuscarCategoriaPorNome buscarCategoriaPorNome;

    public InserirCategoriaUseCase(
            SalvarCategoria salvarCategoria,
            BuscarCategoriaPorNome buscarCategoriaPorNome
    ) {
        this.salvarCategoria = salvarCategoria;
        this.buscarCategoriaPorNome = buscarCategoriaPorNome;
    }

    public void executar(Categoria categoria) {
        buscarCategoriaPorNome.executar(categoria.getNome(), categoria.getUsuarioId())
                .ifPresent(categoriaSalva -> {
                    throw new RuntimeException("Nome de categoria ja cadastrado");
                });
        salvarCategoria.executar(categoria);
    }
}

