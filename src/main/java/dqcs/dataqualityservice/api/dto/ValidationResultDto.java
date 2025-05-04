package dqcs.dataqualityservice.api.dto;

import java.util.List;

public record ValidationResultDto(
        boolean success,
        int evaluatedExpectations,
        int successfulExpectations,
        int unsuccessfulExpectations,
        List<ExpectationResultDto> results
) {}
