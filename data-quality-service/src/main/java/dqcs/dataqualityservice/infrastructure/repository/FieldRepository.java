package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.infrastructure.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findAllByDataSourceId(UUID datasourceId);
}
