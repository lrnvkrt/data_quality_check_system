package dqcs.dataqualityservice.api.dto.field;

import dqcs.dataqualityservice.api.validation.annotation.ValidDataType;
import dqcs.dataqualityservice.api.validation.annotation.ValidFieldName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на создание нового поля в источнике данных")
public record FieldCreateRequest(

        @Schema(
                description = "Имя поля (например, имя колонки в таблице или ключ в сообщении Kafka)",
                example = "order_total",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @ValidFieldName
        String name,

        @Schema(
                description = "Тип данных поля",
                examples = {"float", "boolean", "int"},
                example = "float",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @ValidDataType
        String dataType
) {}
