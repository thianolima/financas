package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarTagPorId;
import br.com.thianolima.infrastructure.provider.database.entity.TagEntity;
import br.com.thianolima.model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarTagPorIdImpl implements BuscarTagPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarTagPorIdImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Tag> executar(Long tagId, Long usuarioId) {
        var consulta = """
                           SELECT t FROM TagEntity t
                           WHERE t.usuarioId = :usuarioId
                           AND t.id = :tagId
                       """;

        return entityManager.createQuery(consulta, TagEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("tagId", tagId)
                .getResultList()
                .stream()
                .map(TagEntity::toModel)
                .findFirst();
    }
}

