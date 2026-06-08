package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.model.DespesaPaginadaItem;
import br.com.thianolima.core.provider.database.BuscarDespesasPorUsuario;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BuscarDespesasPorUsuarioImpl implements BuscarDespesasPorUsuario {

    private final JdbcClient jdbcClient;

    public BuscarDespesasPorUsuarioImpl(
            JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<DespesaPaginadaItem> executar(
            Long usuarioId,
            YearMonth anomes
    ) {
        String sqlNativa =
                "SELECT " +
                "    d.despesa_id as id, " +
                "    d.cartao_id, " +
                "    t.nome as cartao_nome, "+
                "    d.categoria_id, " +
                "    c.nome as categoria_nome, "+
                "    d.descricao_processada as descricao, " +
                "    d.parcela_atual, " +
                "    d.total_parcelas, " +
                "    d.data_vencimento, " +
                "    d.valor, " +
                "    d.observacao, " +
                "    d.recorrente " +
                "FROM tb_despesas d "+
                "LEFT JOIN tb_categorias c ON c.categoria_id = d.categoria_id "+
                "LEFT JOIN tb_cartoes t ON t.cartao_id = d.cartao_id "+
                "WHERE d.usuario_id = :usuarioId " +
                "AND EXTRACT(YEAR FROM d.data_vencimento) = :ano " +
                "AND EXTRACT(MONTH FROM d.data_vencimento) = :mes";

        return jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .param("ano", anomes.getYear())
                .param("mes", anomes.getMonthValue())
                .query(DespesaPaginadaItem.class)
                .list();
    }
}
