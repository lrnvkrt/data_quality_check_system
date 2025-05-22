package dqcs.dataqualityservice.api.dto.datasource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание нового источника данных")
public record DataSourceCreateRequest(

        @Schema(
                description = "Уникальное имя источника (например, имя топика Kafka)",
                example = "orders.topic",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Name must not be blank")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,


        @Schema(
                description = "Дополнительное описание источника данных",
                example = "Kafka топик с заказами клиентов",
                maxLength = 255,
                nullable = true
        )
        @Size(max = 255, message = "Description must be at most 255 characters")
        String description
) {}

