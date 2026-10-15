package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarDespesasPorTag;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarDespesasPorTagImpl implements BuscarDespesasPorTag {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarDespesasPorTagImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Despesa> executar(Long tagId, Long usuarioId) {
        var consulta = """
                           SELECT d FROM DespesaEntity d
                           INNER JOIN d.tags t
                           WHERE d.usuarioId = :usuarioId
                           AND t.id = :tagId
                       """;

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("tagId", tagId)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

