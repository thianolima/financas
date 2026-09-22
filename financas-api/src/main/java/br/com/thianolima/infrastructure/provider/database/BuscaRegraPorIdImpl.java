package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarRegraPorId;
import br.com.thianolima.infrastructure.provider.database.entity.RegraEntity;
import br.com.thianolima.model.Regra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BuscaRegraPorIdImpl implements BuscarRegraPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscaRegraPorIdImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Regra> executar(Long regraId, Long usuarioId) {
        var consulta = """
                          SELECT d FROM RegraEntity d
                          WHERE d.usuarioId = :usuarioId 
                          AND d.id = :regraId
                       """;

        RegraEntity regraEntity = entityManager.createQuery(consulta, RegraEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("regraId", regraId)
                .getSingleResult();

        return regraEntity == null ? Optional.empty() : Optional.of(regraEntity.toModel());
    }
}
