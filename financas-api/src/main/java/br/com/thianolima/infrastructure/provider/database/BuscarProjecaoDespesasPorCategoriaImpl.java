package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.model.ProjecaoDespesaMensalItens;
import br.com.thianolima.core.provider.database.BuscarProjecaoDespesasPorCategoria;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarProjecaoDespesasPorCategoriaImpl implements BuscarProjecaoDespesasPorCategoria {
    private final JdbcClient jdbcClient;

    public BuscarProjecaoDespesasPorCategoriaImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ProjecaoDespesaMensalItens> executar(Long usuarioId) {
        String sqlNativa =
                """                        
                    SELECT 
                        MAX(d.despesa_id) as despesa_id, 
                        null as fatura_id, 
                        d.usuario_id, 
                        d.cartao_id, 
                        t.nome as cartao_nome, 
                        d.categoria_id, 
                        c.nome as categoria_nome, 
                        null as fornecedor_id, 
                        CONCAT('PROJECAO CATEGORIA - ', c.nome) as descricao_original, 
                        CONCAT('PROJECAO CATEGORIA - ', c.nome) as descricao_processada,
                        0 as parcela_atual, 
                        0 as total_parcelas, 
                        0 as sequencia, 
                        CURRENT_DATE() as data_despesa, 
                        CURRENT_DATE() as data_vencimento,
                        SUM(d.valor) as valor,
                        false as recorrente, 
                        'PROJECAO DAS DESPESAS POR CATEGORIA' as observacao 
                    FROM tb_despesas d 
                    LEFT JOIN tb_categorias c on c.categoria_id = d.categoria_id 
                    LEFT JOIN tb_cartoes t on t.cartao_id = d.cartao_id 
                    WHERE d.fatura_id in (SELECT fatura_id FROM (SELECT max(f.fatura_id) as fatura_id, f.cartao_id FROM tb_faturas f GROUP BY f.cartao_id)as faturas) 
                      AND d.total_parcelas = 0 
                      AND d.parcela_atual = 0 
                      AND d.recorrente = false 
                      AND c.incluir_projecao = true 
                      AND d.usuario_id = :usuarioId 
                    GROUP BY 
                        d.usuario_id, 
                        d.cartao_id, 
                        t.nome, 
                        d.categoria_id, 
                        c.nome 
                """;

        return jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .query(ProjecaoDespesaMensalItens.class)
                .list();
    }
}
