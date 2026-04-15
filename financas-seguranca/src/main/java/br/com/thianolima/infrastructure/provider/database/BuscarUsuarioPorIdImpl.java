package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarUsuarioPorId;
import br.com.thianolima.infrastructure.provider.database.entity.UsuarioEntity;
import br.com.thianolima.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarUsuarioPorIdImpl implements BuscarUsuarioPorId {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> executar(Long usuarioId) {
        UsuarioEntity usuarioEntity = entityManager.find(UsuarioEntity.class, usuarioId);
        return usuarioEntity == null ? Optional.empty() : Optional.of(usuarioEntity.toModel());
    }
}
