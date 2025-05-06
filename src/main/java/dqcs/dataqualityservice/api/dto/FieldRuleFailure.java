package dqcs.dataqualityservice.api.dto;

public record FieldRuleFailure(String field, String expectationType, long failed, double rate) {}
