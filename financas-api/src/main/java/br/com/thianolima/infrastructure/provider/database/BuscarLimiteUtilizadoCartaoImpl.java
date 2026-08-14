package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.CartaoLimiteProjection;
import br.com.thianolima.core.provider.database.BuscarLimiteUtilizadoCartao;
import br.com.thianolima.model.BandeiraEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarLimiteUtilizadoCartaoImpl implements BuscarLimiteUtilizadoCartao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<CartaoLimiteProjection> executar(
            Long cartaoId,
            Long usuarioId
    ) {
        var consulta = new StringBuilder(
                """
                    SELECT
                        td.cartao_id,
                        tc.nome,
                        tc.bandeira,
                        tc.numero_final,
                        tc.titular,
                        tc.cor,
                        tc.valor_limite,
                        SUM(td.valor)valor_limite_utilizado
                        FROM tb_despesas td
                    INNER JOIN tb_cartoes tc on tc.cartao_id = td.cartao_id
                    WHERE td.usuario_id = :usuarioId
                    AND td.parcela_atual < td.total_parcelas
                    AND td.fatura_id in (
                        SELECT fatura_id FROM(
                            SELECT 
                                max(f.fatura_id) as fatura_id, f.cartao_id 
                            FROM tb_faturas f 
                            GROUP BY f.cartao_id
                        )as faturas)                    
                """
        );

        var parametros = new MapSqlParameterSource()
                .addValue("usuarioId", usuarioId);

        if (cartaoId != null) {
            consulta.append(" AND td.cartao_id = :cartaoId ");
            parametros.addValue("cartaoId", cartaoId);
        }

        consulta.append(
                 """
                     GROUP BY
                        td.cartao_id,
                        tc.nome,
                        tc.bandeira,
                        tc.numero_final,
                        tc.titular,
                        tc.cor,
                        tc.valor_limite
                """
        );

        return jdbcTemplate.query(consulta.toString(), parametros, (rs, rowNum) -> {
                var limiteCartao = new CartaoLimiteProjection();
                limiteCartao.setCartaoId(rs.getLong("cartao_id"));
                limiteCartao.setNome(rs.getString("nome"));
                limiteCartao.setBandeira(BandeiraEnum.valueOf(rs.getString("bandeira")));
                limiteCartao.setNumeroFinal(rs.getString("numero_final"));
                limiteCartao.setTitular(rs.getString("titular"));
                limiteCartao.setCor(rs.getString("cor"));
                limiteCartao.setValorLimite(rs.getBigDecimal("valor_limite"));
                limiteCartao.setValorLimiteUtilizado(rs.getBigDecimal("valor_limite_utilizado"));
                return limiteCartao;
        });
    }
}
