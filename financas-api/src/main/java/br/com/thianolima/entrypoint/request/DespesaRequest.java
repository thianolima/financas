package br.com.thianolima.entrypoint.request;

import br.com.thianolima.model.Despesa;
import br.com.thianolima.model.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DespesaRequest(
        Long cartaoId,
        Long categoriaId,
        @NotBlank String descricao,
        Integer parcelaAtual,
        Integer totalParcelas,
        @NotNull LocalDate dataDespesa,
        @NotNull LocalDate dataVencimento,
        @NotNull BigDecimal valor,
        String observacao,
        Boolean recorrente,
        List<String> tags
) {
    public Despesa toModel() {
        return Despesa.builder()
                .cartaoId(this.cartaoId)
                .categoriaId(this.categoriaId)
                .descricaoProcessada(this.descricao)
                .parcelaAtual(this.parcelaAtual)
                .totalParcelas(this.totalParcelas)
                .dataDespesa(this.dataDespesa)
                .dataVencimento(this.dataVencimento)
                .valor(this.valor)
                .observacao(this.observacao)
                .recorrente(this.recorrente)
                .tags(this.tags != null ? this.tags.stream().map(tag -> new Tag(null, tag, null)).toList() : null)
                .build();
    }
}