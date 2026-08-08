package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.projection.ProjecaoDespesaMensalItensProjection;
import br.com.thianolima.core.provider.database.BuscarDespesasFuturas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class BuscarDespesasFuturasImpl implements BuscarDespesasFuturas {

    private final JdbcClient jdbcClient;

    public BuscarDespesasFuturasImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ProjecaoDespesaMensalItensProjection> executar(Long usuarioId) {
        String sqlNativa =
                """                        
                    SELECT 
                        MAX(d.despesa_id) as despesa_id, 
                        MAX(d.fatura_id) as fatura_id, 
                        d.usuario_id, 
                        d.cartao_id, 
                        t.nome as cartao_nome, 
                        d.categoria_id, 
                        c.nome as categoria_nome, 
                        MAX(d.descricao_original) as descricao_original, 
                        d.descricao_processada, 
                        MAX(d.parcela_atual) as parcela_atual, 
                        d.total_parcelas, 
                        MAX(d.sequencia) as sequencia, 
                        d.data_despesa, 
                        d.data_vencimento, 
                        d.valor, 
                        d.recorrente, 
                        d.observacao 
                    FROM tb_despesas d 
                    LEFT JOIN tb_categorias c ON c.categoria_id = d.categoria_id 
                    LEFT JOIN tb_cartoes t ON t.cartao_id = d.cartao_id 
                    WHERE d.usuario_id = :usuarioId 
                      AND d.cartao_id IS NULL 
                      AND d.fatura_id IS NULL 
                      AND EXTRACT(YEAR_MONTH FROM d.data_vencimento) > EXTRACT(YEAR_MONTH FROM CURRENT_DATE()) 
                    GROUP BY 
                        d.cartao_id, 
                        t.nome, 
                        d.categoria_id, 
                        c.nome,
                        d.usuario_id,
                        d.descricao_processada,
                        d.observacao,
                        d.total_parcelas,
                        d.data_despesa,
                        d.valor,
                        d.recorrente,
                        d.data_vencimento
                """;

        return jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .query(ProjecaoDespesaMensalItensProjection.class)
                .list();
    }
}
