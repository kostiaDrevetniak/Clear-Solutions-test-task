package com.ClearSolutions.TestTask.validation.validator;

import com.ClearSolutions.TestTask.validation.anotation.ValidAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    public AgeValidator(@Value("${user.valid-age}") Integer validAge) {
        this.validAge = validAge;
    }

    private final Integer validAge;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null)
            return false;
        return birthDate.isBefore(LocalDate.now().minusYears(validAge));
    }
}
