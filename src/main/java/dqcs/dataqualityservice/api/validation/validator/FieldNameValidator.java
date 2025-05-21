package dqcs.dataqualityservice.api.validation.validator;

import dqcs.dataqualityservice.api.validation.annotation.ValidFieldName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldNameValidator implements ConstraintValidator<ValidFieldName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }
}
