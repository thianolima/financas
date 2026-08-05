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
            Integer tamanho
    ){
        this(calcularPagina(itens, pagina, tamanho));
    }

    private PaginaResponse(PaginaResponse paginaResponse) {
        this(
                paginaResponse.paginaAtual,
                paginaResponse.totalPaginas,
                paginaResponse.totalRegistros,
                paginaResponse.registrosPorPagina,
                paginaResponse.items
        );
    }

    private static PaginaResponse calcularPagina(List<?> itens, Integer pagina, Integer tamanho) {
        var totalRegistros = itens.size();
        var totalPaginas = Math.abs(totalRegistros / tamanho);
        var paginaAtual = Math.min(pagina,totalPaginas);
        var inicio = paginaAtual * tamanho;
        var fim = inicio + tamanho;
        var itensPaginado = itens.subList(inicio, Math.min(fim, itens.size()));

        return new PaginaResponse(
                paginaAtual,
                totalPaginas,
                totalRegistros,
                tamanho,
                itensPaginado
        );
    }
}
