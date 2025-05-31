package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpectationRepository extends JpaRepository<Expectation, UUID> {
    List<Expectation> findByFieldId(UUID fieldId);
    List<Expectation> findByFieldDataSourceId(UUID dataSourceId);
    List<Expectation> findByRowConditionIsNotNull();
    List<Expectation> findAllByFieldId(UUID fieldId);
    List<Expectation> findAllByFieldDataSourceIdAndEnabledTrue(UUID dataSourceId);
}
