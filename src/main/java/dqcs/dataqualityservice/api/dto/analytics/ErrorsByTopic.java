package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Агрегация количества ошибок по каждому топику")
public record ErrorsByTopic(
        @Schema(
                description = "Имя топика (источника данных), в котором произошли ошибки",
                example = "orders.topic"
        )
        String topic,

        @Schema(
                description = "Количество ошибок, обнаруженных в этом топике",
                example = "157"
        )
        long errorCount
) { }
