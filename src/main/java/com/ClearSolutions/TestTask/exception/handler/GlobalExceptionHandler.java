package com.ClearSolutions.TestTask.exception.handler;

import com.ClearSolutions.TestTask.exception.NullEntityReferenceException;
import com.ClearSolutions.TestTask.exception.dto.ExceptionResponse;
import com.ClearSolutions.TestTask.exception.dto.ValidationExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler()
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<ValidationExceptionDto> exceptionDtos =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(ValidationExceptionDto::new)
                        .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(exceptionDtos);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentNotValid(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalAccessException(IllegalAccessException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(
                new ExceptionResponse("problem occurred during patching entity"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalHandlerException(Exception ex) {
        log.error(String.format("Failed because of: %s", ex.getMessage()));
        return ResponseEntity.internalServerError().body(new ExceptionResponse(ex));
    }
}
