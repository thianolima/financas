package br.com.thianolima.core.projection;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record ProjecaoDespesaMensalProjection(
        YearMonth anoMes,
        BigDecimal valorTotal,
        BigDecimal valorTotalParcelado,
        BigDecimal valorTotalRecorrente,
        BigDecimal valorTotalAvulso,
        List<ProjecaoDespesaMensalItensProjection> despesas
) {}


