package dqcs.dataqualityservice.api.dto;

public record ValueFailureExample(String value, String field, String description, long count, String sampleError) {}
