package dqcs.dataqualityservice.infrastructure.repository.impl;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationResultDto;
import dqcs.dataqualityservice.api.dto.expectation.ValidationResultDto;
import dqcs.dataqualityservice.config.clickhouse.ClickhouseBatchPreparedStatementSetter;
import dqcs.dataqualityservice.infrastructure.repository.ValidationResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class ClickHouseValidationResultRepository implements ValidationResultRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClickHouseValidationResultRepository(@Qualifier("clickHouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void saveValidationCells(UUID eventId, String topic, List<Map<String, Object>> rows, ValidationResultDto validationResult) {
        final var timestamp = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        List<Object[]> batch = new ArrayList<>();

        for (ExpectationResultDto r: validationResult.results()) {
            Set<Integer> failedIndics = new HashSet<>(r.failedRowIndices());

            for (int i = 0; i < rows.size(); i++) {
                Map<String, Object> row = rows.get(i);
                Object value = row.getOrDefault(r.column(), null);

                boolean isFailure = failedIndics.contains(i);
                int success = isFailure ? 0 : 1;
                String error = isFailure ? r.errorMessage() : null;

                batch.add(new Object[] {
                        eventId.toString(),
                        topic,
                        timestamp,
                        i,
                        r.column(),
                        value,
                        r.description(),
                        r.expectationType(),
                        success,
                        r.severity(),
                        error,
                        r.expectationId().toString()
                });
            }

        }

        jdbcTemplate.batchUpdate("""
            INSERT INTO validation_cell_result
            (event_id, topic, timestamp, row_index, field, value, description, expectation_type, success, severity, error_message, expectation_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
                new ClickhouseBatchPreparedStatementSetter(batch));
    }
}
