package br.com.thianolima.entrypoint.response;

import java.util.List;

public class PaginaResponse {
    private Integer paginaAtual;
    private Integer totalPaginas;
    private Integer totalRegistros;
    private Integer registrosPorPagina;
    private List<?> items;

    public PaginaResponse(
            List<?> itens,
            Integer pagina,
            Integer tamanho
    ){
        var totalRegistros = itens.size();
        var totalPaginas = Math.abs(totalRegistros / tamanho);
        var paginaAtual = Math.min(pagina,totalPaginas);
        var inicio = paginaAtual * tamanho;
        var fim = inicio + tamanho;
        var itensPaginado = itens.subList(inicio, Math.min(fim, itens.size()));

        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
        this.totalRegistros = totalRegistros;
        this.registrosPorPagina = tamanho;
        this.items = itensPaginado;
    }
}
