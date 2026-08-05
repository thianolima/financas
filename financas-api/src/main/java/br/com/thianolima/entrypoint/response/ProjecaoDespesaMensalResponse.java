package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

public record ProjecaoDespesaMensalResponse(List<ProjecaoDespesaMensalItemResponse> data) {

    public record ProjecaoDespesaMensalItemResponse(
            String anoMes,
            BigDecimal valorTotal,
            BigDecimal valorTotalParcelado,
            BigDecimal valorTotalRecorrente,
            BigDecimal valorTotalAvulso,
            List<ProjecaoDespesaMensalItemDespesasResponse> despesas
    ) {}

    public record ProjecaoDespesaMensalItemDespesasResponse(
            String descricao,
            BigDecimal valor,
            String observacao,
            Boolean recorrente,
            Boolean parcelado,
            Boolean avulso,
            Long cartaoId,
            String cartaoNome,
            Integer parcelaAtual,
            Integer totalParcelas,
            LocalDate dataDespesa,
            LocalDate dataVencimento,
            Long categoriaId,
            String categoriaNome
    ) {}

    public ProjecaoDespesaMensalResponse(Collection<ProjecaoDespesaMensalProjection> despesas) {
        this(despesas.stream().map(despesa ->
                new ProjecaoDespesaMensalItemResponse(
                    despesa.anoMes().format(DateTimeFormatter.ofPattern("yyyyMM")),
                    despesa.valorTotal(),
                    despesa.valorTotalParcelado(),
                    despesa.valorTotalRecorrente(),
                    despesa.valorTotalAvulso(),
                    despesa.despesas().stream().map(detalhe ->
                            new ProjecaoDespesaMensalItemDespesasResponse(
                                detalhe.descricaoProcessada(),
                                detalhe.valor(),
                                detalhe.observacao(),
                                detalhe.isRecorrente(),
                                detalhe.isParcelado(),
                                detalhe.isAvulso(),
                                detalhe.cartaoId(),
                                detalhe.cartaoNome(),
                                detalhe.parcelaAtual(),
                                detalhe.totalParcelas(),
                                detalhe.dataDespesa(),
                                detalhe.dataVencimento(),
                                detalhe.categoriaId(),
                                detalhe.categoriaNome()
                            )
                    ).toList()
                )
        ).toList());
    }
}
