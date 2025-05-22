package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о нарушениях конкретного правила на уровне поля")
public record FieldRuleFailure(

        @Schema(
                description = "Имя поля, в котором произошло нарушение",
                example = "order_total"
        )
        String field,

        @Schema(
                description = "Тип правила, вызвавшего ошибку (код из справочника)",
                example = "expect_column_values_to_be_between"
        )
        String expectationType,

        @Schema(
                description = "Количество строк, нарушивших данное правило",
                example = "87"
        )
        long failed,

        @Schema(
                description = "Доля нарушений по этому правилу относительно всех строк (в долях)",
                example = "0.087"
        )
        double rate
) {}
