package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarRegraPorTermoBusca;
import br.com.thianolima.infrastructure.provider.database.entity.RegraEntity;
import br.com.thianolima.model.Regra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarRegraPorTermoBuscaImpl implements BuscarRegraPorTermoBusca {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarRegraPorTermoBuscaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Regra> executar(String termoBusca, Long usuarioId) {
        var consulta = """
                          SELECT r FROM RegraEntity r 
                          JOIN FETCH r.termos t 
                          WHERE r.usuarioId = :usuarioId AND t.termoBusca = :termoBusca
                       """;

        List<RegraEntity> resultados = entityManager.createQuery(consulta, RegraEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("termoBusca", termoBusca)
                .getResultList();

        return resultados.stream()
                .findFirst()
                .map(RegraEntity::toModel);
    }
}
