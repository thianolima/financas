package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BuscarCartaoPorIdImpl implements BuscarCartaoPorId {

    @PersistenceContext
    private EntityManager entityManager;

    public BuscarCartaoPorIdImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Cartao> executar(Long id) {
        CartaoEntity cartaoEntity = entityManager.find(CartaoEntity.class, id);
        return cartaoEntity == null ? Optional.empty() : Optional.of(cartaoEntity.toModel());
    }
}
