package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCategoriasPorUsuario;
import br.com.thianolima.infrastructure.provider.database.entity.CategoriaEntity;
import br.com.thianolima.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarCategoriasPorUsuarioImpl implements BuscarCategoriasPorUsuario {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarCategoriasPorUsuarioImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Categoria> executar(Long usuarioId) {
        var consulta = """
                          SELECT c FROM CategoriaEntity c
                          WHERE (c.usuarioId = :usuarioId OR c.usuarioId = 0) 
                          ORDER BY c.nome ASC
                      """;
        return entityManager.createQuery(consulta, CategoriaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .map(CategoriaEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
