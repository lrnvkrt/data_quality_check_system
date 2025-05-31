package dqcs.dataqualityservice.api.validation.annotation;

import dqcs.dataqualityservice.api.validation.validator.FieldNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFieldName {
    String message() default "Invalid field name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
