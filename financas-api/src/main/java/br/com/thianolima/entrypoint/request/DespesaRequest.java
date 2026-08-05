package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Despesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaRequest(
        Long cartaoId,
        Long categoriaId,
        @NotBlank
        String descricao,
        Integer parcelaAtual,
        Integer totalParcelas,
        @NotNull
        LocalDate dataDespesa,
        @NotNull
        LocalDate dataVencimento,
        @NotNull
        BigDecimal valor,
        String observacao,
        Boolean recorrente
) {

    public DespesaRequest {
        categoriaId = categoriaId != null ? categoriaId : 0L;
        parcelaAtual = parcelaAtual != null ? parcelaAtual : 0;
        totalParcelas = totalParcelas != null ? totalParcelas : 0;
        recorrente = recorrente != null ? recorrente : false;
    }

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
