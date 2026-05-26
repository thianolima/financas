package br.com.thianolima.infrastructure.provider.database;


import br.com.thianolima.core.provider.database.BuscarParcelasAtivasDeCartaoPorUsuario;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BuscarParcelasAtivasDeCartaoPorUsuarioImpl implements BuscarParcelasAtivasDeCartaoPorUsuario {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Despesa> executar(Long usuarioId) {
        String sqlNativa =
                "SELECT " +
                "    MAX(d.despesa_id) as despesa_id, " +
                "    MAX(d.fatura_id) as fatura_id, " +
                "    d.cartao_id, " +
                "    d.usuario_id, " +
                "    MAX(d.categoria_id) as categoria_id, " +
                "    MAX(d.fornecedor_id) as fornecedor_id, " +
                "    MAX(d.descricao_original) as descricao_original, " +
                "    d.descricao_processada, " +
                "    d.observacao, " +
                "    MAX(d.parcela_atual) as parcela_atual, " +
                "    d.total_parcelas, " +
                "    MAX(d.sequencia) as sequencia, " +
                "    d.data_despesa, " +
                "    d.valor, " +
                "    d.recorrente, " +
                "    MAX(d.data_vencimento) as data_vencimento " +
                "FROM tb_despesas d " +
                "WHERE d.usuario_id = :usuarioId " +
                "  AND d.cartao_id IS NOT NULL " +
                "  AND d.fatura_id IS NOT NULL " +
                "  AND d.parcela_atual < d.total_parcelas " +
                "  AND EXTRACT(YEAR_MONTH FROM d.data_vencimento) >= EXTRACT(YEAR_MONTH FROM CURRENT_DATE()) " +
                "GROUP BY " +
                "    d.cartao_id, " +
                "    d.usuario_id, " +
                "    d.descricao_processada, " +
                "    d.observacao, " +
                "    d.total_parcelas, " +
                "    d.data_despesa, " +
                "    d.valor, " +
                "    d.recorrente";

        List<DespesaEntity> entidades = entityManager.createNativeQuery(sqlNativa, DespesaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();

        return entidades.stream()
                .map(DespesaEntity::toModel)
                .collect(Collectors.toList());
    }
}

