package dqcs.dataqualityservice.api.dto;

import java.util.UUID;

public record RuleQualitySummary(UUID expectationId, String field, String expectationType, long failed, long total, double rate) {}

