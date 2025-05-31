package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationDto;

import java.util.List;
import java.util.UUID;

public interface ExpectationService {
    UUID createExpectation(UUID fieldId, ExpectationCreateRequest request);
    List<ExpectationDto> getExpectationsForFieldId(UUID fieldId);
    ExpectationDto getExpectation(UUID expectationId);
    void deleteExpectation(UUID expectationId);
    void toggleExpectation(UUID expectationId);
}
