package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpectationCatalogRepository extends JpaRepository<ExpectationCatalog, UUID> {
}
