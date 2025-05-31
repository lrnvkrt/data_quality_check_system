package dqcs.dataqualityservice.api.validation.annotation;

import dqcs.dataqualityservice.api.validation.validator.NonBlankMapKeysValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NonBlankMapKeysValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NonBlankMapKeys {
    String message() default "Map keys must not be blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
