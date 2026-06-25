package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.ValidarDespesasPertecemUsuario;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidarDespesasPertecemUsuarioImpl implements ValidarDespesasPertecemUsuario {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ValidarDespesasPertecemUsuarioImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean executar(List<Long> despesasIds, Long usuarioId) {
        String sql =
            """
                SELECT COUNT(d.despesa_id) 
                FROM tb_despesas d 
                WHERE d.usuario_id = :usuarioId 
                AND d.despesa_id IN (:ids)
            """;

        var params = new MapSqlParameterSource()
                .addValue("usuarioId", usuarioId)
                .addValue("ids", despesasIds);

        Long quantidadeEncontrada = jdbcTemplate.queryForObject(sql, params, Long.class);

        return quantidadeEncontrada != null && quantidadeEncontrada == despesasIds.size();
    }
}
