package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.api.dto.*;

import java.util.List;
import java.util.UUID;

public interface ValidationAnalyticsRepository {
    List<TopicQualitySummary> getTopicSummaries();
    List<ErrorsByTopic> findErrorsByTopic();
    public TotalAnalytics getTotalAnalytics();

    List<FieldRuleFailure> getFieldFailures(String topic);
    List<FailureTrendPoint> getFailureTrend(String topic, AggregationInterval interval);
    List<ValueFailureExample> getFailureExamples(String topic);

    List<RuleQualitySummary> getRuleSummaries();
    List<FailureTrendPoint> getRuleTrend(UUID expectationId, AggregationInterval interval);
    List<ValueFailureExample> getRuleExamples(UUID expectationId);
}
