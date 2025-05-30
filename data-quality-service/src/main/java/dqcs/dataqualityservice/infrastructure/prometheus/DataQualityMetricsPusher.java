package dqcs.dataqualityservice.infrastructure.prometheus;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationResultDto;

import java.util.List;

public interface DataQualityMetricsPusher {
    void push(String topic, List<ExpectationResultDto> results, int totalRows);
}
