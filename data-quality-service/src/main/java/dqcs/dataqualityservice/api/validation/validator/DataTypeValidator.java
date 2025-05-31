package dqcs.dataqualityservice.api.validation.validator;

import dqcs.dataqualityservice.api.validation.annotation.ValidDataType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataTypeValidator implements ConstraintValidator<ValidDataType, String> {
    private static final String[] ALLOWED = { "string", "int", "integer", "float", "double", "long", "boolean", "date" };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        for (String type : ALLOWED) {
            if (value.equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
