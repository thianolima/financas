package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.ExcluirTag;
import br.com.thianolima.infrastructure.provider.database.entity.TagEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirTagImpl implements ExcluirTag {

    @PersistenceContext
    private final EntityManager entityManager;

    public ExcluirTagImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void executar(Long tagId) {
        entityManager.remove(
                entityManager.find(TagEntity.class, tagId)
        );
    }
}

