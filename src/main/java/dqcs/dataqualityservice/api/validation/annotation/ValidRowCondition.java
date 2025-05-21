package dqcs.dataqualityservice.api.validation.annotation;

import dqcs.dataqualityservice.api.validation.validator.RowConditionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RowConditionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRowCondition {
    String message() default "Row condition is not supported by this expectation type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
