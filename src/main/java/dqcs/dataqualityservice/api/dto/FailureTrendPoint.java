package dqcs.dataqualityservice.api.dto;

import java.time.LocalDateTime;

public record FailureTrendPoint(LocalDateTime timestamp, long failed, long total, double failRate) {}

