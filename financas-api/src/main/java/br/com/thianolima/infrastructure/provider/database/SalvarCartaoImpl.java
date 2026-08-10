package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.SalvarCartao;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalvarCartaoImpl implements SalvarCartao {

    @PersistenceContext
    private final EntityManager entityManager;

    public SalvarCartaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Cartao executar(Cartao cartao) {
        var novoCartao = new CartaoEntity(cartao);
        var cartaoSalvo = entityManager.merge(novoCartao);
        return cartaoSalvo.toModel();
    }
}
