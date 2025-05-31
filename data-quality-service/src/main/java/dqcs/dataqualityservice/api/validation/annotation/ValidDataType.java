package dqcs.dataqualityservice.api.validation.annotation;

import dqcs.dataqualityservice.api.validation.validator.DataTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DataTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDataType {
    String message() default "Unsupported data type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}