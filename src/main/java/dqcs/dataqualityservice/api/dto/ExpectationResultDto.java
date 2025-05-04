package dqcs.dataqualityservice.api.dto;

import java.util.List;
import java.util.UUID;

public record ExpectationResultDto(
        String description,
        String expectationType,
        boolean success,
        String column,
        String errorMessage,
        List<Integer> failedRowIndices,
        String severity,
        UUID expectationId
) {}

