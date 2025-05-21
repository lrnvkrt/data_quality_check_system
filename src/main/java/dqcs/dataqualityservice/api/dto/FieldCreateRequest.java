package dqcs.dataqualityservice.api.dto;

import dqcs.dataqualityservice.api.validation.annotation.ValidDataType;
import dqcs.dataqualityservice.api.validation.annotation.ValidFieldName;
import jakarta.validation.constraints.NotBlank;

public record FieldCreateRequest(

        @NotBlank
        @ValidFieldName
        String name,

        @NotBlank
        @ValidDataType
        String dataType
) {}
