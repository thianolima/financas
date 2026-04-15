package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarFaturaPorId;
import br.com.thianolima.infrastructure.provider.database.entity.FaturaEntity;
import br.com.thianolima.model.Fatura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarFaturaPorIdImpl implements BuscarFaturaPorId {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Fatura> executar(Long faturaId) {
        FaturaEntity cartaoEntity = entityManager.find(FaturaEntity.class, faturaId);
        return cartaoEntity == null ? Optional.empty() : Optional.of(cartaoEntity.toModel());
    }

}
