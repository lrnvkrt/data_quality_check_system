package dqcs.dataqualityservice.api.dto.expectation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Результат выполнения одного правила валидации (expectation)")
public record ExpectationResultDto(

        @Schema(
                description = "Описание правила, заданное пользователем",
                example = "Значения должны быть от 0 до 100 в регионе EU"
        )
        String description,

        @Schema(
                description = "Тип выполненного правила (код из справочника)",
                example = "expect_column_values_to_be_between"
        )
        String expectationType,

        @Schema(
                description = "Флаг успешности выполнения правила: true — пройдено, false — есть нарушения",
                example = "false"
        )
        boolean success,

        @Schema(
                description = "Имя поля (колонки), к которому применялось правило",
                example = "order_total"
        )
        String column,

        @Schema(
                description = "Сообщение об ошибке (если правило не пройдено)",
                example = "Column 'order_total' failed 'expect_column_values_to_be_between' at rows [2, 5]"
        )
        String errorMessage,

        @Schema(
                description = "Список индексов строк, в которых произошло нарушение правила",
                example = "[2, 5, 9]"
        )
        List<Integer> failedRowIndices,

        @Schema(
                description = "Уровень важности нарушения: CRITICAL, WARNING, INFO",
                example = "WARNING"
        )
        String severity,

        @Schema(
                description = "UUID правила в системе",
                example = "f33a9b7e-3e89-4c41-9bc2-9e9aee5c0a7b"
        )
        UUID expectationId
) {}

