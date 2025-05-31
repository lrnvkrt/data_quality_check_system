package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Интервал агрегации для аналитики")
public enum AggregationInterval {

    @Schema(description = "Агрегация по минутам (1 минута)", example = "1 minute")
    MINUTELY("1 minute"),

    @Schema(description = "Агрегация по часам (1 час)", example = "1 hour")
    HOURLY("1 hour"),

    @Schema(description = "Агрегация по дням (1 день)", example = "1 day")
    DAILY("1 day"),

    @Schema(description = "Агрегация по неделям (1 неделя)", example = "1 week")
    WEEKLY("1 week");

    private final String interval;

    AggregationInterval(String interval) {
        this.interval = interval;
    }

    public String sqlInterval() {
        return interval;
    }

    public static AggregationInterval from(String input) {
        return switch (input.toLowerCase()) {
            case "minute", "1 minute", "minutely" -> MINUTELY;
            case "hour", "1 hour", "hourly" -> HOURLY;
            case "day", "1 day", "daily" -> DAILY;
            case "week", "1 week", "weekly" -> WEEKLY;
            default -> throw new IllegalArgumentException("Invalid aggregation interval: " + input);
        };
    }
}
