package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.annotations.NotNull;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegraRequest {
    @NotBlank
    String descricao;

    @NotNull
    Long categoriaId;

    @NotBlank
    String termoBusca;

    public Regra toModel(){
        return Regra.builder()
                .descricao(this.descricao)
                .categoriaId(this.categoriaId)
                .termos(
                   List.of(
                        RegraTermo.builder()
                            .termoBusca(this.termoBusca)
                            .build()
                   )
                )
                .build();
    }

}
