package br.com.thianolima.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public record DespesaMensal(
        YearMonth periodo,
        BigDecimal valorTotal
) { }
