package dqcs.dataqualityservice.application;

import app.grpc.Validation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GrpcValidationService {
    Validation.ValidationResponse validate(UUID dataSourceId, List<Map<String, Object>> rows);
}
