package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Despesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaRequest {
    private Long cartaoId;

    private Long categoriaId = 0l;

    @NotBlank
    private String descricao;

    private Integer parcelaAtual = 0;
    private Integer totalParcelas = 0;

    @NotNull
    private LocalDate dataDespesa;

    @NotNull
    private LocalDate dataVencimento;

    @NotNull
    private BigDecimal valor;

    private String observacao;

    private Boolean recorrente = false;

    public Despesa toModel(){
        return Despesa.builder()
                .cartaoId(cartaoId)
                .categoriaId(categoriaId)
                .descricaoProcessada(descricao)
                .parcelaAtual(parcelaAtual)
                .totalParcelas(totalParcelas)
                .dataDespesa(dataDespesa)
                .dataVencimento(dataVencimento)
                .valor(valor)
                .observacao(observacao)
                .recorrente(recorrente)
                .build();
    }
}
