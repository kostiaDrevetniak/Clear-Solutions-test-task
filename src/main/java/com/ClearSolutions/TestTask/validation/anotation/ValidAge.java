package com.ClearSolutions.TestTask.validation.anotation;

import com.ClearSolutions.TestTask.validation.validator.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAge {
    String message() default "Age must be greater that 18";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
