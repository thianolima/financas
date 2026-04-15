package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarFaturaPorCartaoIdEAnoMes;
import br.com.thianolima.infrastructure.provider.database.entity.FaturaEntity;
import br.com.thianolima.model.Fatura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarFaturaPorCartaoIdEAnoMesImpl implements BuscarFaturaPorCartaoIdEAnoMes {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Fatura> executar(Long cartaoId, String anoMes) {
        var consulta = "SELECT f FROM FaturaEntity f WHERE f.cartaoId = :cartaoId and f.anoMes = :anoMes";
        return entityManager.createQuery(consulta, FaturaEntity.class)
                .setParameter("cartaoId", cartaoId)
                .setParameter("anoMes", anoMes)
                .getResultList()
                .stream()
                .findFirst()
                .map(FaturaEntity::toModel);
    }
}
