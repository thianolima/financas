package br.com.thianolima.infrastructure.provider.database;

import br.com.thianolima.core.provider.database.SalvarTag;
import br.com.thianolima.infrastructure.provider.database.entity.CartaoEntity;
import br.com.thianolima.infrastructure.provider.database.entity.TagEntity;
import br.com.thianolima.model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalvarTagImpl implements SalvarTag {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    @Override
    public Tag executar(Tag tag) {
        var novaTag = new TagEntity(tag);
        var tagSalva = entityManager.merge(novaTag);
        return tagSalva.toModel();
    }
}
