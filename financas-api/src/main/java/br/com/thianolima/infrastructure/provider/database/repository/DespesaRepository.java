package br.com.thianolima.infrastructure.provider.database.repository;

import br.com.thianolima.infrastructure.provider.database.entity.DespesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<DespesaEntity, Long> {
}
