package dqcs.dataqualityservice.infrastructure.repository.impl;

import dqcs.dataqualityservice.api.dto.*;
import dqcs.dataqualityservice.infrastructure.repository.ValidationAnalyticsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ClickHouseValidationAnalyticsRepository  implements ValidationAnalyticsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClickHouseValidationAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TopicQualitySummary> getTopicSummaries() {
        return jdbcTemplate.query("""
        SELECT
            topic,
            total_rows,
            failed_rows,
            round(100.0 * failed_rows / total_rows, 2) AS error_rate
        FROM (
            SELECT
                topic,
                countDistinct(event_id, row_index) AS total_rows,
                countDistinctIf(tuple(event_id, row_index), success = 0) AS failed_rows
            FROM validation_cell_result
            GROUP BY topic
        )
        ORDER BY error_rate DESC
    """, (rs, rowNum) -> new TopicQualitySummary(
                rs.getString("topic"),
                rs.getLong("total_rows"),
                rs.getLong("failed_rows"),
                rs.getDouble("error_rate")
        ));
    }

    @Override
    public List<FieldRuleFailure> getFieldFailures(String topic) {
        return jdbcTemplate.query("""
            SELECT field, expectation_type,
                   countIf(success = 0) AS failed,
                   round(100.0 * failed / count(), 2) AS rate
            FROM validation_cell_result
            WHERE topic = ?
            GROUP BY field, expectation_type
            ORDER BY failed DESC
        """, (rs, rowNum) -> new FieldRuleFailure(
                rs.getString("field"),
                rs.getString("expectation_type"),
                rs.getLong("failed"),
                rs.getDouble("rate")
        ), topic);
    }

    @Override
    public List<FailureTrendPoint> getFailureTrend(String topic, AggregationInterval interval) {
        String sql = String.format("""
        SELECT
            toStartOfInterval(timestamp, INTERVAL %s) AS timestamp,
            countIf(success = 0) AS failed,
            count() AS total,
            round(100.0 * failed / total, 2) AS fail_rate
        FROM validation_cell_result
        WHERE topic = ?
        GROUP BY timestamp
        ORDER BY timestamp
    """, interval.sqlInterval());

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new FailureTrendPoint(
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getLong("failed"),
                        rs.getLong("total"),
                        rs.getDouble("fail_rate")
                ), topic
        );
    }

    @Override
    public List<ValueFailureExample> getFailureExamples(String topic) {
        return jdbcTemplate.query("""
            SELECT value,
                   field,
                   description,
                   count() AS count,
                   any(error_message) AS sample_error
            FROM validation_cell_result
            WHERE topic = ? AND success = 0
            GROUP BY field, value, description
            ORDER BY count DESC
            LIMIT 50
        """, (rs, rowNum) -> new ValueFailureExample(
                rs.getString("value"),
                rs.getString("field"),
                rs.getString("description"),
                rs.getLong("count"),
                rs.getString("sample_error")
        ), topic);
    }

    @Override
    public List<RuleQualitySummary> getRuleSummaries() {
        return jdbcTemplate.query("""
            SELECT expectation_id, field, expectation_type,
                   countIf(success = 0) AS failed,
                   count() AS total,
                   round(100.0 * failed / total, 2) AS rate
            FROM validation_cell_result
            GROUP BY expectation_id, field, expectation_type
            ORDER BY rate DESC
        """, (rs, rowNum) -> new RuleQualitySummary(
                UUID.fromString(rs.getString("expectation_id")),
                rs.getString("field"),
                rs.getString("expectation_type"),
                rs.getLong("failed"),
                rs.getLong("total"),
                rs.getDouble("rate")
        ));
    }

    @Override
    public List<FailureTrendPoint> getRuleTrend(UUID expectationId, AggregationInterval interval) {
        String sql = String.format("""
        SELECT
            toStartOfInterval(timestamp, INTERVAL %s) AS timestamp,
            countIf(success = 0) AS failed,
            count() AS total,
            round(100.0 * failed / total, 2) AS fail_rate
        FROM validation_cell_result
        WHERE expectation_id = ?
        GROUP BY timestamp
        ORDER BY timestamp
    """, interval.sqlInterval());

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new FailureTrendPoint(
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getLong("failed"),
                        rs.getLong("total"),
                        rs.getDouble("fail_rate")
                ),
                expectationId.toString()
        );
    }

    @Override
    public List<ValueFailureExample> getRuleExamples(UUID expectationId) {
        return jdbcTemplate.query("""
            SELECT value,
                   field,
                   description,
                   count() AS count,
                   any(error_message) AS sample_error
            FROM validation_cell_result
            WHERE expectation_id = ? AND success = 0
            GROUP BY value, field, description
            ORDER BY count DESC
            LIMIT 50
        """, (rs, rowNum) -> new ValueFailureExample(
                rs.getString("value"),
                rs.getString("field"),
                rs.getString("description"),
                rs.getLong("count"),
                rs.getString("sample_error")
        ), expectationId.toString());
    }

    @Override
    public List<ErrorsByTopic> findErrorsByTopic() {
        String sql = """
            SELECT topic, count() AS error_count
            FROM dataquality.validation_cell_result
            WHERE success = 0
            GROUP BY topic
            ORDER BY error_count DESC
        """;

        RowMapper<ErrorsByTopic> rowMapper = (rs, rowNum) ->
                new ErrorsByTopic(rs.getString("topic"), rs.getLong("error_count"));

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public TotalAnalytics getTotalAnalytics() {
        String sql = """
            SELECT
                countIf(success = 0) AS error_count,
                count() AS check_count
            FROM dataquality.validation_cell_result
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new TotalAnalytics(
                        rs.getLong("error_count"),
                        rs.getLong("check_count")
                )
        );
    }
}
