package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarDespesasPorCartao;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarDespesasPorCartaoImpl implements BuscarDespesasPorCartao {
    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarDespesasPorCartaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Despesa> executar(Long cartaoId) {
        var consulta = """
                          SELECT d FROM DespesaEntity d 
                          WHERE d.cartaoId = :cartaoId
                       """;
        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("cartaoId", cartaoId)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));    }
}
