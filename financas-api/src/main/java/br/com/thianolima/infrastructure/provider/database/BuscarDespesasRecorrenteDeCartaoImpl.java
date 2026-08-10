package br.com.thianolima.infrastructure.provider.database;



import br.com.thianolima.core.projection.ProjecaoDespesaMensalItensProjection;
import br.com.thianolima.core.provider.database.BuscarDespesasRecorrenteDeCartao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BuscarDespesasRecorrenteDeCartaoImpl implements BuscarDespesasRecorrenteDeCartao {

    private final JdbcClient jdbcClient;

    public BuscarDespesasRecorrenteDeCartaoImpl(JdbcClient jdbcClient) {
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
                         t.cor as cartao_cor,
                         d.categoria_id, 
                         c.nome as categoria_nome, 
                         MAX(d.descricao_original) as descricao_original, 
                         d.descricao_processada, 
                         MAX(d.parcela_atual) as parcela_atual, 
                         d.total_parcelas, 
                         MAX(d.sequencia) as sequencia, 
                         d.data_despesa, 
                         MAX(d.data_vencimento) as data_vencimento, 
                         d.valor, 
                         d.recorrente,
                         d.observacao, 
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
                      AND d.cartao_id IS NOT NULL 
                      AND d.fatura_id IS NOT NULL 
                      AND d.parcela_atual = 0 
                      AND d.total_parcelas = 0 
                      AND d.recorrente IS TRUE 
                      AND d.fatura_id in (SELECT fatura_id FROM (SELECT max(f.fatura_id) as fatura_id, f.cartao_id FROM tb_faturas f GROUP BY f.cartao_id)as faturas) 
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
                        d.recorrente
                """;

        return jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .query(ProjecaoDespesaMensalItensProjection.class)
                .list();

    }
}

