package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.DashboardItemTotaisProjection;
import br.com.thianolima.core.provider.database.BuscarTotaisDespesasDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTotaisDespesasDashboardImpl implements BuscarTotaisDespesasDashboard {

    private final JdbcClient jdbcClient;

    @Override
    public List<DashboardItemTotaisProjection> executar(LocalDate dataReferencia, Long usuarioId) {
        var consulta =
                """                        
                    WITH
                        params AS (
                            SELECT
                                DATE_FORMAT(:dataReferencia, '%Y-%m-01') AS inicio_atual,
                                LAST_DAY(:dataReferencia) AS fim_atual,
                                DATE_FORMAT(DATE_SUB(:dataReferencia, INTERVAL 1 MONTH), '%Y-%m-01') AS inicio_anterior,
                                LAST_DAY(DATE_SUB(:dataReferencia, INTERVAL 1 MONTH)) AS fim_anterior
                        ),
                        tb_mes_atual AS (
                            SELECT
                                CASE
                                    WHEN td.total_parcelas > 0 THEN 'PARCELADO'	
                                    WHEN td.recorrente THEN 'RECORRENTE'
                                    ELSE 'AVULSO'
                                END AS tipo_calculado,
                                SUM(td.valor) AS valor
                            FROM tb_despesas td, params
                            WHERE td.data_vencimento BETWEEN params.inicio_atual AND params.fim_atual
                            AND td.usuario_id = :usuarioId
                            GROUP BY tipo_calculado WITH ROLLUP
                        ),
                        tb_mes_atual_com_total AS (
                            SELECT
                                COALESCE(tipo_calculado, 'TOTAL') AS tipo_despesa,
                                valor
                            FROM tb_mes_atual
                        ),
                        tb_mes_anterior AS (
                            SELECT
                                CASE
                                    WHEN td.total_parcelas > 0 THEN 'PARCELADO'	
                                    WHEN td.recorrente THEN 'RECORRENTE'
                                    ELSE 'AVULSO'
                                END AS tipo_calculado,
                                SUM(td.valor) AS valor
                            FROM tb_despesas td, params
                            WHERE td.data_vencimento BETWEEN params.inicio_anterior AND params.fim_anterior
                            AND td.usuario_id = :usuarioId
                            GROUP BY tipo_calculado WITH ROLLUP
                        ),
                        tb_mes_anterior_com_total AS (
                            SELECT
                                COALESCE(tipo_calculado, 'TOTAL') AS tipo_despesa,
                                valor
                            FROM tb_mes_anterior
                        ),
                        tipos_base AS (
                            SELECT 'AVULSO' AS tipo_despesa
                            UNION ALL SELECT 'PARCELADO'
                            UNION ALL SELECT 'RECORRENTE'
                            UNION ALL SELECT 'TOTAL'
                        )
                    SELECT
                        tb.tipo_despesa,
                        COALESCE(act.valor, 0) AS valor,
                        ROUND(
                            CASE
                                WHEN COALESCE(ant.valor, 0) = 0 THEN 0.0
                                ELSE ((COALESCE(act.valor, 0) - ant.valor) / ant.valor) * 100
                            END, 2
                        ) AS percentual_diferenca
                    FROM tipos_base tb
                    LEFT JOIN tb_mes_atual_com_total act ON act.tipo_despesa = tb.tipo_despesa
                    LEFT JOIN tb_mes_anterior_com_total ant ON ant.tipo_despesa = tb.tipo_despesa;
                """;

        return jdbcClient.sql(consulta)
                .param("usuarioId", usuarioId)
                .param("dataReferencia", dataReferencia)
                .query(DashboardItemTotaisProjection.class)
                .list();
    }
}
