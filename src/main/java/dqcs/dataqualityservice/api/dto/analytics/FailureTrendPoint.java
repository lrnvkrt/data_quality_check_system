package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Точка тренда по качеству данных: количество ошибок и общее число строк во временном интервале")
public record FailureTrendPoint(

        @Schema(
                description = "Метка времени, соответствующая интервалу агрегации",
                example = "2025-05-25T12:00:00",
                format = "date-time"
        )
        LocalDateTime timestamp,

        @Schema(
                description = "Количество строк, не прошедших валидацию в этом интервале",
                example = "25"
        )
        long failed,

        @Schema(
                description = "Общее количество проверенных строк в этом интервале",
                example = "500"
        )
        long total,

        @Schema(
                description = "Доля ошибок в интервале (в долях)",
                example = "0.05"
        )
        double failRate
) {}

