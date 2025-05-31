package dqcs.dataqualityservice.api.validation.validator;

import dqcs.dataqualityservice.api.validation.annotation.NonBlankMapKeys;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class NonBlankMapKeysValidator implements ConstraintValidator<NonBlankMapKeys, Map<String, Object>> {
    @Override
    public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.keySet().stream().allMatch(k -> k != null && !k.trim().isEmpty());
    }
}
