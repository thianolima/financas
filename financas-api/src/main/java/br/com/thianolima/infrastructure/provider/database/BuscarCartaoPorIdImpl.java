package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BuscarCartaoPorIdImpl implements BuscarCartaoPorId {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarCartaoPorIdImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Cartao> executar(Long cartaoId, Long usuarioId) {
        var consulta = """
                          SELECT c FROM CartaoEntity c 
                          WHERE c.usuarioId = :usuarioId AND c.id = :cartaoId
                       """;

        CartaoEntity cartaoEntity = entityManager.createQuery(consulta, CartaoEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("cartaoId", cartaoId)
                .getSingleResult();

        return cartaoEntity == null ? Optional.empty() : Optional.of(cartaoEntity.toModel());
    }
}
