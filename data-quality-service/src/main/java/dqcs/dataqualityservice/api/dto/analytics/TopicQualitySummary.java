package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сводка по качеству данных в топике")
public record TopicQualitySummary(

        @Schema(
                description = "Имя топика (источника данных)",
                example = "orders.topic"
        )
        String topic,

        @Schema(
                description = "Общее количество обработанных строк в топике",
                example = "10000"
        )
        long totalRows,

        @Schema(
                description = "Количество строк, не прошедших хотя бы одно правило валидации",
                example = "843"
        )
        long failedRows,

        @Schema(
                description = "Отношение failedRows к totalRows (в долях)",
                example = "0.0843"
        )
        double errorRate
) {}
