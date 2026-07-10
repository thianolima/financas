package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarDespesaPorId;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarDespesaPorIdImpl implements BuscarDespesaPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarDespesaPorIdImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Despesa> executar(Long despesaId, Long usuarioId) {
        var consulta =
                """
                    SELECT d FROM DespesaEntity d 
                    WHERE (d.usuarioId = :usuarioId OR d.usuarioId = 0) 
                    AND d.id = :despesaId
                """;

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("despesaId", despesaId)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .findFirst();
    }
}
