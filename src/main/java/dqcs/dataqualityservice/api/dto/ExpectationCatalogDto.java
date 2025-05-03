package dqcs.dataqualityservice.api.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

public record ExpectationCatalogDto(
        UUID id,
        String code,
        String name,
        String description,
        JsonNode allowedKwargTypes,
        List<String> allowedArgs,
        boolean requiresNumeric,
        boolean supportsRawCondition
) {}
