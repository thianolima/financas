package br.com.thianolima.core.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaPaginadaItemProjection(
        Long id,
        Long cartaoId,
        String cartaoNome,
        Long categoriaId,
        String cartaoCor,
        String categoriaNome,
        String descricao,
        Integer parcelaAtual,
        Integer totalParcelas,
        LocalDate dataDespesa,
        LocalDate dataVencimento,
        BigDecimal valor,
        String observacao,
        Boolean recorrente
) {

    public boolean isParcelado(){
        return parcelaAtual > 0 && totalParcelas > 0;
    }

    public boolean isRecorrente(){
        return recorrente;
    }

    public boolean isAvulso(){
        return !isRecorrente() && !isParcelado();
    }
}
