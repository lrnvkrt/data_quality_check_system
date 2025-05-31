package dqcs.dataqualityservice.api.dto.expectation;

import dqcs.dataqualityservice.api.validation.annotation.NonBlankMapKeys;
import dqcs.dataqualityservice.api.validation.annotation.ValidRowCondition;
import dqcs.dataqualityservice.api.validation.annotation.ValidSeverity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

@ValidRowCondition
@Schema(description = "Запрос на создание нового правила валидации (expectation)")
public record ExpectationCreateRequest(

        @Schema(
                description = "Идентификатор типа правила из каталога",
                example = "f5d4eeac-cdd9-4635-a65f-185c0a4b39e9",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        UUID expectationTypeId,

        @Schema(
                description = """
            Аргументы, передаваемые в правило.
            Ключи должны быть непустыми, значения — соответствовать ожидаемым типам (int, float, string и т.д.).
            """,
                example = """
            {
              "min_value": 0,
              "max_value": 100,
              "strict": true
            }
            """,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NonBlankMapKeys
        Map<String, Object> kwargs,

        @Schema(
                description = "Человекочитаемое описание правила",
                example = "Значения должны быть от 0 до 100",
                maxLength = 255,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(max = 255, message = "Description must be at most 255 characters")
        String description,

        @Schema(
                description = """
            Условие на уровне строки (SQL-подобный синтаксис), при котором правило применяется.
            Например: region = 'EU' или date > '2024-01-01'.
            """,
                example = "region = 'EU'",
                nullable = true
        )
        String rowCondition,

        @Schema(
                description = "Уровень важности нарушения: CRITICAL, WARNING или INFO",
                example = "WARNING",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @ValidSeverity
        String severity,

        @Schema(
                description = "Флаг активности правила",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        boolean enabled
) {}
