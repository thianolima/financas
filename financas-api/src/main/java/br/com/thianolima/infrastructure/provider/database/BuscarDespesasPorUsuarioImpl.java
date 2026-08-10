package br.com.thianolima.infrastructure.provider.database;


import br.com.thianolima.core.projection.DespesaPaginadaItemProjection;
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
    public List<DespesaPaginadaItemProjection> executar(
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
                    t.cor as cartao_cor,
                    d.categoria_id, 
                    c.nome as categoria_nome, 
                    d.descricao_processada as descricao, 
                    d.parcela_atual, 
                    d.total_parcelas,
                    d.data_despesa, 
                    d.data_vencimento, 
                    d.valor, 
                    d.observacao, 
                    d.recorrente,
                    dt.tags                    
                FROM tb_despesas d 
                LEFT JOIN tb_categorias c ON c.categoria_id = d.categoria_id 
                LEFT JOIN tb_cartoes t ON t.cartao_id = d.cartao_id 
                LEFT JOIN (
                    SELECT
                        dr.despesa_id,
                        GROUP_CONCAT(tg.nome ORDER BY tg.nome) AS tags
                    FROM tb_despesas_tags dr
                    LEFT JOIN tb_tags tg ON tg.tag_id = dr.tag_id
                    GROUP BY dr.despesa_id
                ) dt ON dt.despesa_id = d.despesa_id
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

        sql.append(" ORDER BY d.data_vencimento DESC ");

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            var tagsConcat = rs.getString("tags");

            List<String> tags = (tagsConcat == null || tagsConcat.isBlank())
                    ? List.of()
                    : java.util.Arrays.stream(tagsConcat.split(","))
                    .filter(s -> s != null && !s.isBlank())
                    .toList();

            return DespesaPaginadaItemProjection.builder()
                    .id(rs.getLong("id"))
                    .cartaoId(rs.getLong("cartao_id"))
                    .cartaoNome(rs.getString("cartao_nome"))
                    .cartaoCor(rs.getString("cartao_cor"))
                    .categoriaId(rs.getLong("categoria_id"))
                    .categoriaNome(rs.getString("categoria_nome"))
                    .descricao(rs.getString("descricao"))
                    .parcelaAtual(rs.getInt("parcela_atual"))
                    .totalParcelas(rs.getInt("total_parcelas"))
                    .dataDespesa(rs.getDate("data_despesa").toLocalDate())
                    .dataVencimento(rs.getDate("data_vencimento").toLocalDate())
                    .valor(rs.getBigDecimal("valor"))
                    .observacao(rs.getString("observacao"))
                    .recorrente(rs.getBoolean("recorrente"))
                    .tags(tags)
                    .build();
        });
    }
}
