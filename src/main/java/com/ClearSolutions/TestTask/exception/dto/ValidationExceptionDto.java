package com.ClearSolutions.TestTask.exception.dto;

import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ValidationExceptionDto {
    private String name;
    private String message;

    public ValidationExceptionDto(FieldError fieldError) {
        this.name = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}

