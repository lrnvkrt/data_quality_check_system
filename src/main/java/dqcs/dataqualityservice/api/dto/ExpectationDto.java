package dqcs.dataqualityservice.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ExpectationDto(
        UUID expectationId,
        UUID fieldId,
        UUID expectationTypeId,
        String expectationCode,
        Map<String, Object> kwargs,
        String description,
        String severity,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String rowCondition,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
