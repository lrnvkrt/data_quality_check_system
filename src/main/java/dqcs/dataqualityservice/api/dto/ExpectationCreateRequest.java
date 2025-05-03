package dqcs.dataqualityservice.api.dto;

import java.util.Map;
import java.util.UUID;

public record ExpectationCreateRequest(
        UUID expectationTypeId,
        Map<String, Object> kwargs,
        String description,
        String rowCondition,
        String severity,
        boolean enabled
) {}
