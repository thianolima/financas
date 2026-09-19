package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.DashboardItemTotaisParcelasProjection;
import br.com.thianolima.core.projection.DashboardItemTotaisProjection;
import br.com.thianolima.core.provider.database.BuscarTotaisPrimeiraUltimaParcelaDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTotaisPrimeiraUltimaParcelaDashboardImpl implements BuscarTotaisPrimeiraUltimaParcelaDashboard {

    private final JdbcClient jdbcClient;

    @Override
    public List<DashboardItemTotaisParcelasProjection> executar(LocalDate dataReferencia, Long usuarioId) {
        var consulta =
                """
                        SELECT
                            SUM(td.valor) AS valorTotal,
                            COUNT(td.despesa_id) as quantidade,
                           	'NOVA' as tipo_parcela
                        FROM tb_despesas td
                        WHERE td.data_vencimento BETWEEN DATE_FORMAT(:dataReferencia, '%Y-%m-01') AND LAST_DAY(:dataReferencia)
                        AND td.usuario_id = :usuarioId
                        AND td.parcela_atual = 1
                        AND td.total_parcelas > 0
                        UNION ALL
                        SELECT
                            SUM(td.valor) AS valorTotal,
                            COUNT(td.despesa_id) as quantidade,
                           	'FINALIZADA' as tipo_parcela
                        FROM tb_despesas td
                        WHERE td.data_vencimento BETWEEN DATE_FORMAT(:dataReferencia, '%Y-%m-01') AND LAST_DAY(:dataReferencia)
                        AND td.usuario_id = :usuarioId
                        AND td.parcela_atual = td.total_parcelas
                        AND td.total_parcelas > 0
                """;

        return jdbcClient.sql(consulta)
                .param("usuarioId", usuarioId)
                .param("dataReferencia", dataReferencia)
                .query(DashboardItemTotaisParcelasProjection.class)
                .list();
    }
}
