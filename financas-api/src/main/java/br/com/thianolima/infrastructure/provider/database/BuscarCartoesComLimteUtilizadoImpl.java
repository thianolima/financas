package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalItensProjection;
import br.com.thianolima.core.provider.database.BuscarCartoesComLimteUtilizado;
import org.springframework.jdbc.core.simple.JdbcClient;

public class BuscarCartoesComLimteUtilizadoImpl implements BuscarCartoesComLimteUtilizado {

    private final JdbcClient jdbcClient;

    public BuscarCartoesComLimteUtilizadoImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void executar(Long usuarioId) {
        String sqlNativa =
                """
                        SELECT
                        tc.cartao_id,
                        tc.valor_limite,
                        sum(td.valor) as total_gasto,
                        round((sum(td.valor) * 100)/ tc.valor_limite,2) as percentual_utilizado
                        FROM tb_despesas td
                        INNER JOIN tb_cartoes tc ON tc.cartao_id = td.cartao_id
                        WHERE td.usuario_id = :usuarioId 
                        AND td.parcela_atual < td.total_parcelas
                        AND td.fatura_id IN (
                        	SELECT fatura_id FROM (
                        	  SELECT max(f.fatura_id) as fatura_id, f.cartao_id
                        	  FROM tb_faturas f
                        	  GROUP BY f.cartao_id
                        ) AS faturas)
                        GROUP BY td.cartao_id, fatura_id, tc.valor_limite
                """;

        jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .query(ProjecaoDespesaMensalItensProjection.class)
                .list();
    }
}
