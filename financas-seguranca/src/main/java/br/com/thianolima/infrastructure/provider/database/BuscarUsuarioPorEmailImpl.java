package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.BuscarUsuarioPorEmail;
import br.com.thianolima.infrastructure.provider.database.entity.UsuarioEntity;
import br.com.thianolima.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarUsuarioPorEmailImpl implements BuscarUsuarioPorEmail {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> execute(String email) {
        var consulta = "SELECT u FROM UsuarioEntity u WHERE u.email = :email";
        var usuarioEntity = entityManager.createQuery(consulta, UsuarioEntity.class)
                .setParameter("email", email)
                .getSingleResult();
        return usuarioEntity == null ? Optional.empty() : Optional.of(usuarioEntity.toModel());
    }
}
