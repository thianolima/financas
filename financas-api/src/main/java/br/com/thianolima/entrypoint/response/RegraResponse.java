package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;

import java.util.List;

public record RegraResponse(
        Long id,
        Long categoriaId,
        String descricao,
        List<RegraTermo> termos
) {
    public RegraResponse(Regra regra) {
        this(
                regra.getId(),
                regra.getCategoriaId(),
                regra.getDescricao(),
                regra.getTermos()
        );
    }
}
