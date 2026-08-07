package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.projection.DespesaPaginadaItemProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DespesaPaginadaItemResponse(
        Long id,
        Long cartaoId,
        String cartaoNome,
        String cartaoCor,
        Long categoriaId,
        String categoriaNome,
        String descricao,
        Integer parcelaAtual,
        Integer totalParcelas,
        LocalDate dataDespesa,
        LocalDate dataVencimento,
        BigDecimal valor,
        String observacao,
        Boolean recorrente,
        Boolean avulso,
        Boolean parcelado,
        List<String> tags
) {

    public DespesaPaginadaItemResponse(DespesaPaginadaItemProjection despesaPaginadaItemProjection){
        this(
                despesaPaginadaItemProjection.getId(),
                despesaPaginadaItemProjection.getCartaoId(),
                despesaPaginadaItemProjection.getCartaoNome(),
                despesaPaginadaItemProjection.getCartaoCor(),
                despesaPaginadaItemProjection.getCategoriaId(),
                despesaPaginadaItemProjection.getCategoriaNome(),
                despesaPaginadaItemProjection.getDescricao(),
                despesaPaginadaItemProjection.getParcelaAtual(),
                despesaPaginadaItemProjection.getTotalParcelas(),
                despesaPaginadaItemProjection.getDataDespesa(),
                despesaPaginadaItemProjection.getDataVencimento(),
                despesaPaginadaItemProjection.getValor(),
                despesaPaginadaItemProjection.getObservacao(),
                despesaPaginadaItemProjection.isRecorrente(),
                despesaPaginadaItemProjection.isAvulso(),
                despesaPaginadaItemProjection.isParcelado(),
                despesaPaginadaItemProjection.getTags()
        );
    }
}
