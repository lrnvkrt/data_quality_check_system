package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.*;
import dqcs.dataqualityservice.infrastructure.repository.ValidationAnalyticsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
public class ValidationAnalyticsController {

    private final ValidationAnalyticsRepository repository;

    public ValidationAnalyticsController(ValidationAnalyticsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/topics/errors")
    public List<ErrorsByTopic> getErrorsByTopic() {
        return repository.findErrorsByTopic();
    }

    @GetMapping("/topics/total")
    public TotalAnalytics getTotalAnalytics() {
        return repository.getTotalAnalytics();
    }

    @GetMapping("/topics")
    public List<TopicQualitySummary> getTopics() {
        return repository.getTopicSummaries();
    }

    @GetMapping("/topics/{topic}/fields")
    public List<FieldRuleFailure> getFieldFailures(@PathVariable String topic) {
        return repository.getFieldFailures(topic);
    }

    @GetMapping("/topics/{topic}/trend")
    public List<FailureTrendPoint> getTrend(@PathVariable String topic, @RequestParam(defaultValue = "1 hour") String interval
    ) {
        return repository.getFailureTrend(topic, AggregationInterval.from(interval));
    }

    @GetMapping("/topics/{topic}/examples")
    public List<ValueFailureExample> getFailureExamples(@PathVariable String topic) {
        return repository.getFailureExamples(topic);
    }

    @GetMapping("/rules")
    public List<RuleQualitySummary> getRuleSummaries() {
        return repository.getRuleSummaries();
    }

    @GetMapping("/rules/{expectationId}/trend")
    public List<FailureTrendPoint> getRuleTrend(@PathVariable UUID expectationId, @RequestParam(defaultValue = "1 hour") String interval) {
        return repository.getRuleTrend(expectationId, AggregationInterval.from(interval));
    }

    @GetMapping("/rules/{expectationId}/examples")
    public List<ValueFailureExample> getRuleExamples(@PathVariable UUID expectationId) {
        return repository.getRuleExamples(expectationId);
    }
}
