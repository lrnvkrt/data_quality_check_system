package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.api.dto.ValidationResultDto;

import java.util.List;

public interface BatchValidationService {
    ValidationResultDto validate(String topic, List<GenericEvent> events);
}
