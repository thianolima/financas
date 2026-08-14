package br.com.thianolima.infrastructure.provider.database;


import br.com.thianolima.core.projection.DashboardItemHistoricoProjection;
import br.com.thianolima.core.projection.DashboardItemTotaisProjection;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasPorHistoricoDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTotaisDespesasPorHistoricoDashboardImpl implements BuscarTotaisDespesasPorHistoricoDashboard {

    private final JdbcClient jdbcClient;

    public List<DashboardItemHistoricoProjection> executar(LocalDate dataReferencia, Long usuarioId) {
           var consulta =
               """
                    WITH
                        parametros AS (
                            SELECT
                                DATE_FORMAT(:dataReferencia, '%Y-%m-01') AS data_atual,
                                DATE_SUB(:dataReferencia, INTERVAL 1 MONTH) AS data_inicio,
                                LAST_DAY(DATE_ADD(:dataReferencia, INTERVAL 1 MONTH)) AS data_fim
                        )
                        SELECT
                            SUM(valor) as valorTotal,
                            EXTRACT(MONTH FROM td.data_vencimento) mes,
                            false AS projecao
                        FROM tb_despesas td, parametros p
                        WHERE td.data_vencimento BETWEEN p.data_inicio AND p.data_fim
                        AND td.usuario_id = :usuarioId
                        GROUP BY mes
                    
                """;

        return jdbcClient.sql(consulta)
                .param("usuarioId", usuarioId)
                .param("dataReferencia", dataReferencia)
                .query(DashboardItemHistoricoProjection.class)
                .list();
    }
}
