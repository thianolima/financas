package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarParcelaAnterior;
import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import br.com.thianolima.model.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BuscarParcelaAnteriorImpl implements BuscarParcelaAnterior {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Despesa> executar(
            LocalDate dataDespesa,
            BigDecimal valor,
            Long cartaoId,
            Integer parcela
    ) {
        var consulta = "SELECT d FROM DespesaEntity d " +
                       "WHERE d.cartaoId = :cartaoId " +
                       "and d.valor = :valor " +
                       "and d.dataDespesa = :dataDespesa " +
                       "and d.parcelaAtual <= :parcela ";

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("cartaoId", cartaoId)
                .setParameter("valor", valor)
                .setParameter("dataDespesa", dataDespesa)
                .setParameter("parcela", parcela)
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .findFirst();
    }
}
