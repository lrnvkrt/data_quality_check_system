USE default;

CREATE DATABASE IF NOT EXISTS dataquality;

USE dataquality;

CREATE TABLE validation_cell_result
(
    event_id         UUID,
    topic            String,
    timestamp        DateTime,
    row_index        UInt32,
    field            String,
    value            String,
    description      String,
    expectation_type String,
    success          UInt8,
    severity         String,
    error_message    String,
    expectation_id   String
)
    ENGINE = MergeTree
    ORDER BY (topic, timestamp, row_index, field);
