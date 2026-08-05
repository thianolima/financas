package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegraRequest(
        @NotBlank
        String descricao,
        @NotNull
        Long categoriaId,
        @NotBlank
        String termoBusca
) {

    public Regra toModel(){
        return Regra.builder()
                .descricao(descricao)
                .categoriaId(categoriaId)
                .termos(
                   List.of(
                        RegraTermo.builder()
                            .termoBusca(termoBusca)
                            .build()
                   )
                )
                .build();
    }

}
