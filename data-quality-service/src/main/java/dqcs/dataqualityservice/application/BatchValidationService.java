package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.api.dto.expectation.ValidationResultDto;

import java.util.List;

public interface BatchValidationService {
    ValidationResultDto validate(String topic, List<GenericEvent> events);
}
