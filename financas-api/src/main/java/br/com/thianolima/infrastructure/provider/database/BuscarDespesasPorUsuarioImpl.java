package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.model.DespesaPaginadaItem;
import br.com.thianolima.core.model.TipoDespesaEnum;
import br.com.thianolima.core.provider.database.BuscarDespesasPorUsuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BuscarDespesasPorUsuarioImpl implements BuscarDespesasPorUsuario {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BuscarDespesasPorUsuarioImpl(
            NamedParameterJdbcTemplate  jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DespesaPaginadaItem> executar(
            Long usuarioId,
            YearMonth anomes,
            Long cartaoId,
            Long categoriaId,
            TipoDespesaEnum tipo
    ) {
        var sql = new StringBuilder(
            """
                SELECT 
                    d.despesa_id as id, 
                    d.cartao_id, 
                    t.nome as cartao_nome, 
                    d.categoria_id, 
                    c.nome as categoria_nome, 
                    d.descricao_processada as descricao, 
                    d.parcela_atual, 
                    d.total_parcelas, 
                    d.data_vencimento, 
                    d.valor, 
                    d.observacao, 
                    d.recorrente 
                FROM tb_despesas d 
                LEFT JOIN tb_categorias c ON c.categoria_id = d.categoria_id 
                LEFT JOIN tb_cartoes t ON t.cartao_id = d.cartao_id 
                WHERE d.usuario_id = :usuarioId 
                AND EXTRACT(YEAR FROM d.data_vencimento) = :ano 
                AND EXTRACT(MONTH FROM d.data_vencimento) = :mes
            """
        );

        var params = new MapSqlParameterSource()
                .addValue("usuarioId", usuarioId)
                .addValue("ano", anomes.getYear())
                .addValue("mes", anomes.getMonthValue());

        if (cartaoId != null) {
            sql.append(" AND d.cartao_id = :cartaoId ");
            params.addValue("cartaoId", cartaoId);
        }

        if (categoriaId != null) {
            sql.append(" AND d.categoria_id = :categoriaId ");
            params.addValue("categoriaId", categoriaId);
        }

        if (tipo != null) {
            switch (tipo) {
                case RECORRENTE -> sql.append(" AND d.recorrente = true ");
                case AVULSO     -> sql.append(" AND d.recorrente = false AND d.total_parcelas = 0 ");
                case PARCELADO  -> sql.append(" AND d.total_parcelas > 0 ");
            }
        }

        return jdbcTemplate.query(
                    sql.toString(),
                    params,
                    new BeanPropertyRowMapper<>(DespesaPaginadaItem.class)
               );
    }
}
