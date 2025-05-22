package dqcs.dataqualityservice.api.dto.datasource;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO с информацией об источнике данных")
public record DataSourceDto(
        @Schema(
                description = "Уникальный идентификатор источника данных",
                example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
        )
        UUID id,

        @Schema(
                description = "Имя источника данных (например, Kafka топик)",
                example = "orders.topic"
        )
        String name,

        @Schema(
                description = "Описание источника данных",
                example = "Kafka топик с заказами клиентов",
                nullable = true
        )
        String description
) {}
