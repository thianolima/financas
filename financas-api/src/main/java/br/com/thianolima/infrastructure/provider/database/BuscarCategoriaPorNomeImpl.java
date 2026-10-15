package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorNome;
import br.com.thianolima.infrastructure.provider.database.entity.CategoriaEntity;
import br.com.thianolima.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarCategoriaPorNomeImpl implements BuscarCategoriaPorNome {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarCategoriaPorNomeImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Categoria> executar(String nome, Long usuarioId) {
        var consulta = """
                           SELECT c FROM CategoriaEntity c
                           WHERE c.usuarioId = :usuarioId
                           AND c.nome = :nome
                       """;

        return entityManager.createQuery(consulta, CategoriaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("nome", nome)
                .getResultList()
                .stream()
                .map(CategoriaEntity::toModel)
                .findFirst();
    }
}

