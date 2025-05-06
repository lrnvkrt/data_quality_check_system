package dqcs.dataqualityservice.api.dto;

public record TotalAnalytics(
        long errorCount,
        long checkCount
) {
}
