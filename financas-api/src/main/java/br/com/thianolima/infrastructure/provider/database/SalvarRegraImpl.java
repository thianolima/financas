package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.SalvarRegra;
import br.com.thianolima.infrastructure.provider.database.entity.RegraEntity;
import br.com.thianolima.model.Regra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalvarRegraImpl implements SalvarRegra {

    @PersistenceContext
    private final EntityManager entityManager;

    public SalvarRegraImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Regra executar(Regra regra) {
        var novaRegra = new RegraEntity(regra);
        var regraSalva = entityManager.merge(novaRegra);
        return regraSalva.toModel();
    }
}
