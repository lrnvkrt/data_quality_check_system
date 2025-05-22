package dqcs.dataqualityservice.api.dto.expectation;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Результат пакетной валидации: общее состояние и список проверенных правил")
public record ValidationResultDto(

        @Schema(
                description = "Флаг общей успешности: true, если все правила прошли без нарушений",
                example = "false"
        )
        boolean success,

        @Schema(
                description = "Общее количество выполненных правил валидации",
                example = "5"
        )
        int evaluatedExpectations,

        @Schema(
                description = "Количество успешно пройденных правил",
                example = "3"
        )
        int successfulExpectations,

        @Schema(
                description = "Количество правил, вызвавших ошибки",
                example = "2"
        )
        int unsuccessfulExpectations,

        @ArraySchema(
                schema = @Schema(implementation = ExpectationResultDto.class),
                arraySchema = @Schema(description = "Список результатов выполнения всех правил")
        )
        List<ExpectationResultDto> results
) {}
