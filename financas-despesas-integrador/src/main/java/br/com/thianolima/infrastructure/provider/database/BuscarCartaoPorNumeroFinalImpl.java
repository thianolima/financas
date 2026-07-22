package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarCartaoPorNumeroFinal;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.infrastructure.provider.database.entity.FaturaEntity;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarCartaoPorNumeroFinalImpl implements BuscarCartaoPorNumeroFinal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cartao> executar(String numeroFinal, Long usuarioId) {
        var consulta = "SELECT c FROM CartaoEntity c WHERE c.numeroFinal = :numeroFinal and c.usuarioId = :usuarioId";
        return entityManager.createQuery(consulta, CartaoEntity.class)
                .setParameter("numeroFinal", numeroFinal)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .findFirst()
                .map(CartaoEntity::toModel);
    }
}
