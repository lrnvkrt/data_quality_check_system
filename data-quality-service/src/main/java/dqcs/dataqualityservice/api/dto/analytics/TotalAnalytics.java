package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Общая сводная аналитика по валидациям")
public record TotalAnalytics(
        @Schema(
                description = "Общее количество ошибок, зафиксированных системой",
                example = "4321"
        )
        long errorCount,
        @Schema(
                description = "Общее количество выполненных проверок (expectation checks)",
                example = "15000"
        )
        long checkCount
) {
}
