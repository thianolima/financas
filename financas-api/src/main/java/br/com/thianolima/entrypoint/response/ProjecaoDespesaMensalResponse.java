package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ProjecaoDespesaMensalResponse {
    private final List<ProjecaoDespesaMensalItemResponse> data;

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
            String cartaoCor,
            Integer parcelaAtual,
            Integer totalParcelas,
            LocalDate dataDespesa,
            LocalDate dataVencimento,
            Long categoriaId,
            String categoriaNome,
            List<String> tags
    ) {}

    public ProjecaoDespesaMensalResponse(List<ProjecaoDespesaMensalProjection> despesas) {
        this.data = despesas.stream().map(despesa ->
                new ProjecaoDespesaMensalItemResponse(
                        despesa.getAnoMes().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        despesa.getValorTotal(),
                        despesa.getValorTotalParcelado(),
                        despesa.getValorTotalRecorrente(),
                        despesa.getValorTotalAvulso(),
                        despesa.getDespesas().stream().map(detalhe ->
                                new ProjecaoDespesaMensalItemDespesasResponse(
                                        detalhe.getDescricaoProcessada(),
                                        detalhe.getValor(),
                                        detalhe.getObservacao(),
                                        detalhe.isRecorrente(),
                                        detalhe.isParcelado(),
                                        detalhe.isAvulso(),
                                        detalhe.getCartaoId(),
                                        detalhe.getCartaoNome(),
                                        detalhe.getCartaoCor(),
                                        detalhe.getParcelaAtual(),
                                        detalhe.getTotalParcelas(),
                                        detalhe.getDataDespesa(),
                                        detalhe.getDataVencimento(),
                                        detalhe.getCategoriaId(),
                                        detalhe.getCategoriaNome(),
                                        detalhe.getTags()
                                )
                        ).toList()
                )
        ).toList();
    }
}