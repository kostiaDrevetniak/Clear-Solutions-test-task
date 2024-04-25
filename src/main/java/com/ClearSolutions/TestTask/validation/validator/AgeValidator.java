package com.ClearSolutions.TestTask.validation.validator;

import com.ClearSolutions.TestTask.validation.anotation.ValidAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    @Value("${user.valid-age}")
    public int validAge;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null)
            return false;
        return birthDate.isBefore(LocalDate.now().minusYears(validAge));
    }
}
