package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.ExcluirCartao;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirCartaoImpl implements ExcluirCartao {

    @PersistenceContext
    private final EntityManager entityManager;

    public ExcluirCartaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void executar(Long cartaoId) {
        entityManager.remove(
                entityManager.find(CartaoEntity.class, cartaoId)
        );
    }
}
