package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarDespesaPorFaturaIdESequencia;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarDespesaPorFaturaIdESequenciaImpl implements BuscarDespesaPorFaturaIdESequencia {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Despesa> executar(
            Integer sequencia,
            Long faturaId
    ) {
        var consulta = "SELECT d FROM DespesaEntity d WHERE d.sequencia = :sequencia and d.faturaId = :faturaId";

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("sequencia", sequencia)
                .setParameter("faturaId", faturaId)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .findFirst();
    }
}
