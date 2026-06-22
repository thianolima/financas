package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarDespesaPorId;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BuscarDespesaPorIdImpl implements BuscarDespesaPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarDespesaPorIdImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Despesa> executar(Long despesaId, Long usuarioId) {
        var consulta = """
                          SELECT d FROM DespesaEntity d
                          WHERE d.usuarioId = :usuarioId AND d.id = :despesaId
                       """;

        DespesaEntity despesaEntity = entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("despesaId", despesaId)
                .getSingleResult();

        return despesaEntity == null ? Optional.empty() : Optional.of(despesaEntity.toModel());
    }
}
