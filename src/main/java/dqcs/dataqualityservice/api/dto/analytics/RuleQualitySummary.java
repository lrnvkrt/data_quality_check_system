package dqcs.dataqualityservice.api.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Сводная статистика по правилу валидации (expectation)")
public record RuleQualitySummary(

        @Schema(
                description = "UUID правила валидации (expectation)",
                example = "c18f1d17-3d7e-4bd3-8b51-d2f68fcb739d"
        )
        UUID expectationId,

        @Schema(
                description = "Имя поля, к которому привязано правило",
                example = "order_total"
        )
        String field,

        @Schema(
                description = "Тип правила валидации (код из справочника)",
                example = "expect_column_values_to_be_between"
        )
        String expectationType,

        @Schema(
                description = "Количество строк, не прошедших проверку по этому правилу",
                example = "120"
        )
        long failed,

        @Schema(
                description = "Общее количество строк, на которые применялось правило",
                example = "1000"
        )
        long total,

        @Schema(
                description = "Доля нарушений по правилу (в долях)",
                example = "0.12"
        )
        double rate
) {}

