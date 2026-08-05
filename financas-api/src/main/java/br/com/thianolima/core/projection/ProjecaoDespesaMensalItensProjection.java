package br.com.thianolima.core.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjecaoDespesaMensalItensProjection(
        Long id,
        Long faturaId,
        Long usuarioId,
        Long cartaoId,
        String cartaoNome,
        Long categoriaId,
        String categoriaNome,
        Long fornecedorId,
        String descricaoOriginal,
        String descricaoProcessada,
        Integer parcelaAtual,
        Integer totalParcelas,
        Integer sequencia,
        LocalDate dataDespesa,
        LocalDate dataVencimento,
        BigDecimal valor,
        String observacao,
        Boolean recorrente
) {

    public ProjecaoDespesaMensalItensProjection(
            Long usuarioId,
            String descricaoOriginal,
            String descricaoProcessada,
            Long categoriaId,
            String categoriaNome,
            Long cartaoId,
            String cartaoNome,
            Integer parcelaAtual,
            Integer totalParcelas,
            LocalDate dataVencimento,
            String observacao,
            Boolean recorrente,
            BigDecimal valor
    ) {
        this(
                null,
                null,
                usuarioId,
                cartaoId,
                cartaoNome,
                categoriaId,
                categoriaNome,
                null,
                descricaoOriginal,
                descricaoProcessada,
                parcelaAtual,
                totalParcelas,
                null,
                null,
                dataVencimento,
                valor,
                observacao,
                recorrente
        );
    }

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


