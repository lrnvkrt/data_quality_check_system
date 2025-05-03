package dqcs.dataqualityservice.api.dto;

public record FieldCreateRequest(
        String name,
        String dataType
) {}
