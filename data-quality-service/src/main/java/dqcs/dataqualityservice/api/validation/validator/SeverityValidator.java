package dqcs.dataqualityservice.api.validation.validator;

import dqcs.dataqualityservice.api.validation.annotation.ValidSeverity;
import dqcs.dataqualityservice.infrastructure.entity.Severity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class SeverityValidator implements ConstraintValidator<ValidSeverity, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.stream(Severity.values())
                .anyMatch(s -> s.name().equalsIgnoreCase(value));
    }
}