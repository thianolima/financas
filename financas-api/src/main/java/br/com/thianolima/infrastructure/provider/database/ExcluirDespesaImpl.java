package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.ExcluirDespesa;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExcluirDespesaImpl implements ExcluirDespesa {

    @PersistenceContext
    private final EntityManager entityManager;

    public ExcluirDespesaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void executar(Long despesaId) {
        entityManager.remove(
                entityManager.find(DespesaEntity.class, despesaId)
        );
    }
}
