package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorId;
import br.com.thianolima.core.provider.database.BuscarDespesasPorCategoria;
import br.com.thianolima.core.provider.database.ExcluirCategoria;

public class ExcluirCategoriaUseCase {

    private final ExcluirCategoria excluirCategoria;
    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final BuscarDespesasPorCategoria buscarDespesasPorCategoria;

    public ExcluirCategoriaUseCase(
            ExcluirCategoria excluirCategoria,
            BuscarCategoriaPorId buscarCategoriaPorId,
            BuscarDespesasPorCategoria buscarDespesasPorCategoria
    ) {
        this.excluirCategoria = excluirCategoria;
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.buscarDespesasPorCategoria = buscarDespesasPorCategoria;
    }

    public void executar(Long categoriaId, Long usuarioId) {
        var categoriaSalva = buscarCategoriaPorId.executar(categoriaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));

        if (!categoriaSalva.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Categoria nao encontrada");
        }

        if (!buscarDespesasPorCategoria.executar(categoriaId).isEmpty()) {
            throw new RuntimeException("Nao se pode excluir uma categoria com despesas vinculadas");
        }

        excluirCategoria.executar(categoriaId);
    }
}
