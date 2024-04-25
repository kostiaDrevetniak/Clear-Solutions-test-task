package com.ClearSolutions.TestTask.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String message;

    public ExceptionResponse(Exception ex) {
        this.message = ex.getMessage();
    }
}
