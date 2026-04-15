package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarDespesaRecorrente;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BuscarDespesaRecorrenteImpl implements BuscarDespesaRecorrente {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Despesa> executar(
            String descricaoOriginal,
            BigDecimal valor,
            Long cartaoId
    ) {
        var consulta = "SELECT d FROM DespesaEntity d " +
                "WHERE d.cartaoId = :cartaoId " +
                "and d.valor = :valor " +
                "and d.descricaoOriginal = :descricaoOriginal ";

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("cartaoId", cartaoId)
                .setParameter("valor", valor)
                .setParameter("descricaoOriginal", descricaoOriginal)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .findFirst();
    }
}
