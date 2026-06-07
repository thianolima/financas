package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarCartoesPorUsuario;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.model.Cartao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarCartoesPorUsuarioImpl implements BuscarCartoesPorUsuario {

    @PersistenceContext
    private final EntityManager entityManager;

    public BuscarCartoesPorUsuarioImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Cartao> executar(Long usuarioId) {
        var consulta = "SELECT f FROM CartaoEntity f WHERE f.usuario.id = :usuarioId";
        return entityManager.createQuery(consulta, CartaoEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .map(CartaoEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
