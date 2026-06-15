package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorId;
import br.com.thianolima.infrastructure.provider.database.entity.CategoriaEntity;
import br.com.thianolima.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BuscarCategoriaPorIdImpl implements BuscarCategoriaPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarCategoriaPorIdImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Categoria> executar(Long categoriaId, Long usuarioId) {
        var consulta = """
                          SELECT c FROM CategoriaEntity c 
                          WHERE (c.usuarioId = :usuarioId OR c.usuarioId = 0) 
                          AND c.id = :categoriaId
                       """;

        CategoriaEntity categoriaEntity = entityManager.createQuery(consulta, CategoriaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("categoriaId", categoriaId)
                .getSingleResult();

        return categoriaEntity == null ? Optional.empty() : Optional.of(categoriaEntity.toModel());
    }
}
