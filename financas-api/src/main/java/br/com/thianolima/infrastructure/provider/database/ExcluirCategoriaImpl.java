package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.ExcluirCategoria;
import br.com.thianolima.infrastructure.provider.database.entity.CategoriaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirCategoriaImpl implements ExcluirCategoria {

    @PersistenceContext
    private final EntityManager entityManager;

    public ExcluirCategoriaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void executar(Long categoriaId) {
        entityManager.remove(
                entityManager.find(CategoriaEntity.class, categoriaId)
        );
    }
}

