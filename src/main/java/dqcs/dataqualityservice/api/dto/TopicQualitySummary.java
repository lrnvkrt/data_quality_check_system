package dqcs.dataqualityservice.api.dto;

public record TopicQualitySummary(String topic, long totalRows, long failedRows, double errorRate) {}
