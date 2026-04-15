package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.SalvarFatura;
import br.com.thianolima.infrastructure.provider.database.entity.FaturaEntity;
import br.com.thianolima.model.Fatura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalvarFaturaImpl implements SalvarFatura {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Fatura executar(Fatura fatura) {
        var faturaEntity = new FaturaEntity(fatura);
        return entityManager.merge(faturaEntity).toModel();
    }
}
