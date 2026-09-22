package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarRegrasPorUsuario;
import br.com.thianolima.infrastructure.provider.database.entity.RegraEntity;
import br.com.thianolima.model.Regra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarRegrasPorUsuarioImpl implements BuscarRegrasPorUsuario {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarRegrasPorUsuarioImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Regra> executar(Long usuarioId) {
        var consulta = """
                          SELECT r FROM RegraEntity r
                          JOIN FETCH r.termos t
                          WHERE r.usuarioId = :usuarioId
                       """;

        return entityManager.createQuery(consulta, RegraEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .map(RegraEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
