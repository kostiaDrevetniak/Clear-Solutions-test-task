package com.ClearSolutions.TestTask.validation;

import com.ClearSolutions.TestTask.validation.validator.AgeValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AgeValidatorTest {
    @Autowired
    AgeValidator ageValidator;

    @Test
    public void validBirthDate() {
        ConstraintValidatorContext mock = Mockito.mock(ConstraintValidatorContext.class);

        boolean valid = ageValidator.isValid(LocalDate.of(2005, 1, 19), mock);

        assertTrue(valid);
    }

    @Test
    public void validateWrongBirthDate() {
        ConstraintValidatorContext mock = Mockito.mock(ConstraintValidatorContext.class);

        boolean valid = ageValidator.isValid(LocalDate.of(2015, 1, 19), mock);

        assertFalse(valid);
    }

    @Test
    public void validateNullBirthDate() {
        ConstraintValidatorContext mock = Mockito.mock(ConstraintValidatorContext.class);

        boolean valid = ageValidator.isValid(null, mock);

        assertFalse(valid);
    }
}
