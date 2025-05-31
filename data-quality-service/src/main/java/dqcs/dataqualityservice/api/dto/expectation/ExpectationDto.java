package dqcs.dataqualityservice.api.dto.expectation;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ExpectationDto(
        @Schema(
                description = "UUID правила (expectation)",
                example = "a7b0c6ae-3f15-4039-abe6-0b4e98362a79"
        )
        UUID expectationId,

        @Schema(
                description = "UUID поля, к которому привязано правило",
                example = "3fdfc8c0-2e3b-4fc1-aeec-0d2232b3abfa"
        )
        UUID fieldId,

        @Schema(
                description = "UUID типа правила из каталога",
                example = "f5d4eeac-cdd9-4635-a65f-185c0a4b39e9"
        )
        UUID expectationTypeId,

        @Schema(
                description = "Кодовое имя правила (например, expect_column_values_to_be_between)",
                example = "expect_column_values_to_be_between"
        )
        String expectationCode,

        @Schema(
                description = """
            Аргументы, передаваемые в правило (имя → значение).
            Типы значений могут быть строками, числами, логическими значениями и списками.
            """,
                example = """
            {
              "min_value": 10,
              "max_value": 100,
              "strict": true
            }
            """
        )
        Map<String, Object> kwargs,

        @Schema(
                description = "Описание правила, заданное пользователем",
                example = "Значения колонки должны быть от 10 до 100"
        )
        String description,

        @Schema(
                description = "Уровень важности нарушения: CRITICAL, WARNING или INFO",
                example = "WARNING"
        )
        String severity,

        @Schema(
                description = "Условие фильтрации строк, при котором применяется правило (если задано)",
                example = "region = 'EU'",
                nullable = true
        )
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String rowCondition,

        @Schema(
                description = "Флаг, включено ли правило",
                example = "true"
        )
        boolean enabled,

        @Schema(
                description = "Дата и время создания правила",
                example = "2025-05-24T12:00:00",
                format = "date-time"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Дата и время последнего изменения",
                example = "2025-05-25T09:30:00",
                format = "date-time"
        )
        LocalDateTime modifiedAt
) {}
