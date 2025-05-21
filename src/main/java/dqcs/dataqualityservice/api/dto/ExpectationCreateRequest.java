package dqcs.dataqualityservice.api.dto;

import dqcs.dataqualityservice.api.validation.annotation.NonBlankMapKeys;
import dqcs.dataqualityservice.api.validation.annotation.ValidRowCondition;
import dqcs.dataqualityservice.api.validation.annotation.ValidSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

@ValidRowCondition
public record ExpectationCreateRequest(

        @NotNull
        UUID expectationTypeId,

        @NonBlankMapKeys
        Map<String, Object> kwargs,

        @NotBlank
        @Size(max = 255, message = "Description must be at most 255 characters")
        String description,

        String rowCondition,

        @NotBlank
        @ValidSeverity
        String severity,

        boolean enabled
) {}
