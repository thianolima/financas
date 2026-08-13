package br.com.thianolima.core.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardProjection {
    List<DashboardItemCategoriaProjection> cardRankingCategorias;
    List<DashboardItemCategoriaProjection> cardDespesasPorCategoria;
    List<DashboardItemTotaisProjection> cardTotaisDespesas;
}


