package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.projection.DespesaPaginadaItemProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        Boolean parcelado
) {

    public DespesaPaginadaItemResponse(DespesaPaginadaItemProjection despesaPaginadaItemProjection){
        this(
                despesaPaginadaItemProjection.id(),
                despesaPaginadaItemProjection.cartaoId(),
                despesaPaginadaItemProjection.cartaoNome(),
                despesaPaginadaItemProjection.cartaoCor(),
                despesaPaginadaItemProjection.categoriaId(),
                despesaPaginadaItemProjection.categoriaNome(),
                despesaPaginadaItemProjection.descricao(),
                despesaPaginadaItemProjection.parcelaAtual(),
                despesaPaginadaItemProjection.totalParcelas(),
                despesaPaginadaItemProjection.dataDespesa(),
                despesaPaginadaItemProjection.dataVencimento(),
                despesaPaginadaItemProjection.valor(),
                despesaPaginadaItemProjection.observacao(),
                despesaPaginadaItemProjection.isRecorrente(),
                despesaPaginadaItemProjection.isAvulso(),
                despesaPaginadaItemProjection.isParcelado()
        );
    }
}
