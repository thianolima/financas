package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.DashboardItemCategoriaProjection;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasPorCategoriaDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTotaisDespesasPorCategoriaDashboardImpl implements BuscarTotaisDespesasPorCategoriaDashboard {

    private final JdbcClient jdbcClient;

    @Override
    public List<DashboardItemCategoriaProjection> executar(LocalDate dataReferencia, Long usuarioId) {
        var consulta =
                """
                    SELECT
                        tc.nome AS categoriaNome,
                        SUM(td.valor) AS valorTotal,
                        ROUND((SUM(td.valor) / SUM(SUM(td.valor)) OVER()) * 100, 2) AS percentual
                    FROM tb_despesas td
                    INNER JOIN tb_categorias tc ON tc.categoria_id = td.categoria_id
                    WHERE td.data_vencimento BETWEEN DATE_FORMAT(:dataReferencia, '%Y-%m-01') AND LAST_DAY(:dataReferencia)
                    AND td.usuario_id = :usuarioId
                    GROUP BY tc.nome
                    ORDER BY percentual DESC
                """;
        return jdbcClient.sql(consulta)
                .param("usuarioId", usuarioId)
                .param("dataReferencia", dataReferencia)
                .query(DashboardItemCategoriaProjection.class)
                .list();
    }
}
