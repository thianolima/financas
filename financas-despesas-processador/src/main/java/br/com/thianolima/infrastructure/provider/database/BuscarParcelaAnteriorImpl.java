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
    public Optional<Despesa> executar(Despesa despesa) {
        var consulta =
           """
               SELECT d FROM DespesaEntity d
               WHERE d.cartaoId = :cartaoId
               AND d.valor = :valor
               AND d.dataDespesa < :dataDespesa
               AND d.parcelaAtual < :parcela
           """;

        return entityManager.createQuery(consulta, DespesaEntity.class)
                .setParameter("cartaoId", despesa.getCartaoId())
                .setParameter("valor", despesa.getValor())
                .setParameter("dataDespesa", despesa.getDataDespesa())
                .setParameter("parcela", despesa.getParcelaAtual())
                .getResultList()
                .stream()
                .map(DespesaEntity::toModel)
                .findFirst();
    }
}
