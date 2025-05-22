package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.analytics.*;
import dqcs.dataqualityservice.infrastructure.repository.ValidationAnalyticsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics")
public class ValidationAnalyticsController {

    private final ValidationAnalyticsRepository repository;

    public ValidationAnalyticsController(ValidationAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Operation(
            summary = "Ошибки по топикам",
            description = "Возвращает количество ошибок по каждому топику (источнику данных)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = ErrorsByTopic.class)))
            }
    )
    @GetMapping("/topics/errors")
    public List<ErrorsByTopic> getErrorsByTopic() {
        return repository.findErrorsByTopic();
    }


    @Operation(
            summary = "Общая аналитика по всем топикам",
            description = "Возвращает суммарную статистику: общее число ошибок, правил, полей и топиков",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = TotalAnalytics.class)))
            }
    )
    @GetMapping("/topics/total")
    public TotalAnalytics getTotalAnalytics() {
        return repository.getTotalAnalytics();
    }


    @Operation(
            summary = "Качество по всем топикам",
            description = "Возвращает список топиков и сводную информацию о качестве данных в каждом из них",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = TopicQualitySummary.class)))
            }
    )
    @GetMapping("/topics")
    public List<TopicQualitySummary> getTopics() {
        return repository.getTopicSummaries();
    }

    @Operation(
            summary = "Ошибки по полям внутри топика",
            description = "Показывает, какие поля чаще всего нарушают правила в конкретном топике",
            parameters = {
                    @Parameter(name = "topic", description = "Имя топика", example = "orders.topic", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = FieldRuleFailure.class)))
            }
    )
    @GetMapping("/topics/{topic}/fields")
    public List<FieldRuleFailure> getFieldFailures(@PathVariable String topic) {
        return repository.getFieldFailures(topic);
    }

    @Operation(
            summary = "Тренд ошибок по топику",
            description = "Агрегирует количество ошибок по временным интервалам в пределах одного топика",
            parameters = {
                    @Parameter(name = "topic", description = "Имя топика", example = "orders.topic", required = true),
                    @Parameter(name = "interval", description = "Интервал агрегации (например: 1 hour, 1 day)", example = "1 hour")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = FailureTrendPoint.class)))
            }
    )
    @GetMapping("/topics/{topic}/trend")
    public List<FailureTrendPoint> getTrend(@PathVariable String topic, @RequestParam(defaultValue = "1 hour") String interval
    ) {
        return repository.getFailureTrend(topic, AggregationInterval.from(interval));
    }

    @Operation(
            summary = "Примеры невалидных значений по топику",
            description = "Показывает конкретные примеры значений, нарушающих правила, сгруппированные по полям",
            parameters = {
                    @Parameter(name = "topic", description = "Имя топика", example = "orders.topic", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = ValueFailureExample.class)))
            }
    )
    @GetMapping("/topics/{topic}/examples")
    public List<ValueFailureExample> getFailureExamples(@PathVariable String topic) {
        return repository.getFailureExamples(topic);
    }

    @Operation(
            summary = "Общая статистика по правилам",
            description = "Возвращает список всех правил с указанием доли провалов, количества применений и т. д.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = RuleQualitySummary.class)))
            }
    )
    @GetMapping("/rules")
    public List<RuleQualitySummary> getRuleSummaries() {
        return repository.getRuleSummaries();
    }

    @Operation(
            summary = "Тренд ошибок по конкретному правилу",
            description = "Агрегирует количество ошибок, связанных с указанным правилом, по времени",
            parameters = {
                    @Parameter(name = "expectationId", description = "UUID правила", example = "a0f9b8e2-b33e-4e3f-8f71-3a1ad4b3cdef", required = true),
                    @Parameter(name = "interval", description = "Интервал агрегации (например: 1 hour, 1 day)", example = "1 hour")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = FailureTrendPoint.class)))
            }
    )
    @GetMapping("/rules/{expectationId}/trend")
    public List<FailureTrendPoint> getRuleTrend(@PathVariable UUID expectationId, @RequestParam(defaultValue = "1 hour") String interval) {
        return repository.getRuleTrend(expectationId, AggregationInterval.from(interval));
    }

    @Operation(
            summary = "Примеры нарушений по конкретному правилу",
            description = "Показывает конкретные значения, нарушающие заданное правило",
            parameters = {
                    @Parameter(name = "expectationId", description = "UUID правила", example = "a0f9b8e2-b33e-4e3f-8f71-3a1ad4b3cdef", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = ValueFailureExample.class)))
            }
    )
    @GetMapping("/rules/{expectationId}/examples")
    public List<ValueFailureExample> getRuleExamples(@PathVariable UUID expectationId) {
        return repository.getRuleExamples(expectationId);
    }
}
