package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarFornecedoresPorUsuarioId;
import br.com.thianolima.infrastructure.provider.database.entity.FornecedorEntity;
import br.com.thianolima.model.Fornecedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarFornecedoresPorUsuarioIdImpl implements BuscarFornecedoresPorUsuarioId {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Fornecedor> executar(Long usuarioId) {
        var consulta = "SELECT f FROM FornecedorEntity f WHERE f.usuarioId = :usuarioId or f.usuarioId = 0";
        return entityManager.createQuery(consulta, FornecedorEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .map(FornecedorEntity::toModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
