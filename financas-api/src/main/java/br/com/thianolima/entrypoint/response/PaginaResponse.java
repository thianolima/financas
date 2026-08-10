package br.com.thianolima.entrypoint.response;

import java.util.List;

public record PaginaResponse(
        Integer paginaAtual,
        Integer totalPaginas,
        Integer totalRegistros,
        Integer registrosPorPagina,
        List<?> items
) {
    public PaginaResponse(
            List<?> itens,
            Integer pagina,
            Integer registrosPorPagina
    ) {
        this(
                calcularPaginaAtual(itens, pagina, registrosPorPagina),
                calcularTotalPaginas(itens, registrosPorPagina),
                itens.size(),
                registrosPorPagina,
                calcularItensPaginados(itens, pagina, registrosPorPagina)
        );
    }

    private static int calcularTotalPaginas(
            List<?> itens,
            Integer tamanho
    ) {
        return Math.abs(itens.size() / tamanho);
    }

    private static int calcularPaginaAtual(List<?> itens, Integer pagina, Integer registrosPorPagina) {
        var totalPaginas = calcularTotalPaginas(itens, registrosPorPagina);
        return Math.min(pagina, totalPaginas);
    }

    private static List<?> calcularItensPaginados(List<?> itens, Integer pagina, Integer registrosPorPagina) {
        int paginaAtual = calcularPaginaAtual(itens, pagina, registrosPorPagina);
        int inicio = paginaAtual * registrosPorPagina;
        int fim = inicio + registrosPorPagina;
        return itens.subList(inicio, Math.min(fim, itens.size()));
    }
}
