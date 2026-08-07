package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.BuscarTagPorNome;
import br.com.thianolima.infrastructure.provider.database.entity.TagEntity;
import br.com.thianolima.model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarTagPorNomeImpl implements BuscarTagPorNome {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Tag> executar(
            String nome,
            Long usuarioId
    ) {
        var consulta = """
                          SELECT t FROM TagEntity t 
                          WHERE t.usuarioId = :usuarioId 
                          AND t.nome = :nome
                       """;

        return entityManager.createQuery(consulta, TagEntity.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("nome", nome)
                .getResultList()
                .stream()
                .map(TagEntity::toModel)
                .findFirst();
    }
}
