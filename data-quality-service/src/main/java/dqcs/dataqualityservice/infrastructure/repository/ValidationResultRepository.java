package dqcs.dataqualityservice.infrastructure.repository;

import dqcs.dataqualityservice.api.dto.expectation.ValidationResultDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ValidationResultRepository {
    void saveValidationCells(UUID eventId, String topic, List<Map<String, Object>> rows, ValidationResultDto validationResult);
}
