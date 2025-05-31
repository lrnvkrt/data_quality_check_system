package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, UUID> {
    boolean existsByName(String name);
    Optional<DataSource> findByName(String name);
}
