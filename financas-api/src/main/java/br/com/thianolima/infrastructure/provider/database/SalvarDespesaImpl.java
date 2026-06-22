package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.SalvarDespesa;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalvarDespesaImpl implements SalvarDespesa {

    @PersistenceContext
    private final EntityManager entityManager;

    public SalvarDespesaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Despesa executar(Despesa despesa) {
        var novaDespesa = new DespesaEntity(despesa);
        var despesaAtualziada = entityManager.merge(novaDespesa);
        return despesaAtualziada.toModel();
    }
}
