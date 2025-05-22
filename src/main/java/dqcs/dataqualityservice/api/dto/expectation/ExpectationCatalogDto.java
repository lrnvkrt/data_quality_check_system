package dqcs.dataqualityservice.api.dto.expectation;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Запись каталога правил проверки качества данных (expectation)")
public record ExpectationCatalogDto(
        @Schema(
                description = "Уникальный идентификатор правила",
                example = "c2718e2f-b7f2-4e77-ae61-88e8c7de5001"
        )
        UUID id,

        @Schema(
                description = "Машинное имя правила (используется в коде)",
                example = "expect_column_values_to_be_between"
        )
        String code,

        @Schema(
                description = "Человекочитаемое название правила",
                example = "СНИЛС"
        )
        String name,

        @Schema(
                description = "Описание логики правила",
                example = "Проверяет, что значение поля является СНИЛС-ом"
        )
        String description,

        @Schema(
                description = """
            Сопоставление допустимых аргументов и их типов.
            Ключ — имя аргумента; значение — тип: int, float, string, boolean, list и т. д.
            """,
                example = """
            {
              "min_value": "float",
              "max_value": "float",
              "strict": "boolean"
            }
            """
        )
        JsonNode allowedKwargTypes,

        @Schema(
                description = "Список имён аргументов, допустимых для использования в этом правиле",
                example = "[\"min_value\", \"max_value\", \"strict\"]"
        )
        List<String> allowedArgs,

        @Schema(
                description = "Требует ли правило числовые значения в колонке",
                example = "true"
        )
        boolean requiresNumeric,

        @Schema(
                description = "Поддерживает ли правило логическое условие для отбора строк (row condition)",
                example = "true"
        )
        boolean supportsRawCondition
) {}
