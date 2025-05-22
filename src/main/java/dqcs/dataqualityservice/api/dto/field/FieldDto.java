package dqcs.dataqualityservice.api.dto.field;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record FieldDto(
        @Schema(
                description = "Уникальный идентификатор поля",
                example = "9432155e-47e4-4a1a-bdfc-b48ef2d33c11"
        )
        UUID id,

        @Schema(
                description = "Имя поля",
                example = "order_total"
        )
        String name,

        @Schema(
                description = "Тип данных поля",
                example = "float"
        )
        String dataType
) {}
