package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarDespesasPorCategoria;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarDespesasPorCategoriaImpl implements BuscarDespesasPorCategoria {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarDespesasPorCategoriaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Despesa> executar(Long categoriaId) {
        var consulta = """
                           SELECT d FROM DespesaEntity d
                           WHERE d.categoriaId = :categoriaId
                       """;

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("categoriaId", categoriaId)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

