package dqcs.dataqualityservice.api.dto;

public enum AggregationInterval{
    MINUTELY("1 minute"),
    HOURLY("1 hour"),
    DAILY("1 day"),
    WEEKLY ("1 week");

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
