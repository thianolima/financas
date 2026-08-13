package br.com.thianolima.core.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardItemCategoriaProjection {
    private String categoriaNome;
    private BigDecimal valorTotal;
    private BigDecimal percentual;
}
