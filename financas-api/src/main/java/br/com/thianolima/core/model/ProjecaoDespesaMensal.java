package br.com.thianolima.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjecaoDespesaMensal {
    YearMonth anoMes;
    BigDecimal valorTotal;
    BigDecimal valorTotalParcelado;
    BigDecimal valorTotalRecorrente;
    BigDecimal valorTotalAvulso;
    List<ProjecaoDespesaMensalItens> despesas;
}


