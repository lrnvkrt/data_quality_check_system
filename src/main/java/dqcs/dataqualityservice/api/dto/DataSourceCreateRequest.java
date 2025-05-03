package dqcs.dataqualityservice.api.dto;

public record DataSourceCreateRequest(
        String name,
        String description
) {}
