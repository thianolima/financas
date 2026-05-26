package br.com.thianolima.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespesaMensal {
    YearMonth anoMes;
    BigDecimal valorTotal;
}


