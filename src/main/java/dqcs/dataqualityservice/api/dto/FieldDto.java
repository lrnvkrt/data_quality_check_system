package dqcs.dataqualityservice.api.dto;

import java.util.UUID;

public record FieldDto(
        UUID id,
        String name,
        String dataType
) {}
