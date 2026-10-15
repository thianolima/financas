package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.SalvarCategoria;
import br.com.thianolima.infrastructure.provider.database.entity.CategoriaEntity;
import br.com.thianolima.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalvarCategoriaImpl implements SalvarCategoria {

    @PersistenceContext
    private final EntityManager entityManager;

    public SalvarCategoriaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Categoria executar(Categoria categoria) {
        var novaCategoria = new CategoriaEntity(categoria);
        var categoriaSalva = entityManager.merge(novaCategoria);
        return categoriaSalva.toModel();
    }
}

