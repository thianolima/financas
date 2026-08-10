package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarTagsPorUsuarioId;
import br.com.thianolima.infrastructure.provider.database.entity.TagEntity;
import br.com.thianolima.model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTagsPorUsuarioImpl implements BuscarTagsPorUsuarioId {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Tag> executar(
            Long usuarioId
    ) {
        var consulta = """
                          SELECT t FROM TagEntity t 
                          WHERE t.usuarioId = :usuarioId 
                       """;

        return entityManager.createQuery(consulta, TagEntity.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList()
                .stream()
                .map(TagEntity::toModel)
                .toList();
    }

}
