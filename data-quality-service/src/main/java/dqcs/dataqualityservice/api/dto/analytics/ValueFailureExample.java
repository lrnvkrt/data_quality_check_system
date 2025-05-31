package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Пример значения, нарушающего правило валидации")
public record ValueFailureExample(

        @Schema(
                description = "Нарушающее значение, зафиксированное в данных",
                example = "-1.0"
        )
        String value,

        @Schema(
                description = "Имя поля, в котором было найдено значение",
                example = "order_total"
        )
        String field,

        @Schema(
                description = "Описание правила, которое было нарушено",
                example = "Значения должны быть больше или равны 0"
        )
        String description,

        @Schema(
                description = "Количество повторений этого нарушения (значение встречалось столько раз)",
                example = "42"
        )
        long count,

        @Schema(
                description = "Пример сообщения об ошибке для данного значения",
                example = "Column 'order_total' failed 'expect_column_values_to_be_between' at rows [12, 54, 199] (values: [-1.0, -2.5, -3.1])"
        )
        String sampleError
) {}
