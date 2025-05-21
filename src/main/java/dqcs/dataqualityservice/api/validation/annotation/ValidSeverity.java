package dqcs.dataqualityservice.api.validation.annotation;

import dqcs.dataqualityservice.api.validation.validator.SeverityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SeverityValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSeverity {
    String message() default "Invalid severity level";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}