package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarRegraPorTermo;
import br.com.thianolima.model.Regra;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarRegraPorTermoImpl implements BuscarRegraPorTermo {

    private final JdbcClient jdbcClient;

    public BuscarRegraPorTermoImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Regra> executar(String termoBusca, Long usuarioId) {
        String sqlNativa =
                """
                    SELECT 
                          r.regra_id,
                          r.usuario_id,
                          r.categoria_id,
                          r.descricao
                     FROM tb_regras r
                     JOIN tb_regras_termos rt ON r.regra_id = rt.regra_id
                    WHERE :usuarioId IN (r.usuario_id, 0)
                      AND INSTR(UPPER(:termoBusca), UPPER(rt.termo_busca)) > 0                        
                    ORDER BY 
                          r.usuario_id DESC,
                          LENGTH(rt.termo_busca) DESC
                    LIMIT 1;
                """;

        return jdbcClient.sql(sqlNativa)
                .param("usuarioId", usuarioId)
                .param("termoBusca", termoBusca)
                .query(Regra.class)
                .list()
                .stream()
                .findFirst();
    }
}
