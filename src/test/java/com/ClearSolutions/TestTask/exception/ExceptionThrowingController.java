package com.ClearSolutions.TestTask.exception;

import com.ClearSolutions.TestTask.dto.request.UserRequest;
import com.ClearSolutions.TestTask.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("test")
public class ExceptionThrowingController {
    @Autowired
    private Validator validator;

    @PostMapping("/methodArgumentNotValidException")
    public void throwMethodArgumentNotValidException(@Valid @RequestBody UserRequest userRequest) {
    }

    @PostMapping("/constraintViolationException")
    public void throwConstraintViolationException(@RequestBody User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<User> constraintViolation : violations.stream()
                .sorted(Comparator.comparing(ConstraintViolation::getMessage)).collect(Collectors.toList())) {
            sb.append("\t").append(constraintViolation.getMessage()).append("\n");
        }
        throw new ConstraintViolationException("Error occurred:\n" + sb.toString(), violations);
    }

    @GetMapping("nullEntityReferenceException")
    public void throwNullEntityReferenceException() {
        throw new NullEntityReferenceException(GlobalExceptionHandlerTest.DEFAULT_EXCEPTION_MESSAGE);
    }

    @GetMapping("entityNotFoundException")
    public void throwEntityNotFoundException() {
        throw new EntityNotFoundException(GlobalExceptionHandlerTest.DEFAULT_EXCEPTION_MESSAGE);
    }

    @GetMapping("illegalArgumentException")
    public void throwIllegalArgumentException() {
        throw new IllegalArgumentException(GlobalExceptionHandlerTest.DEFAULT_EXCEPTION_MESSAGE);
    }

    @GetMapping("illegalAccessException")
    public void throwIllegalAccessException() throws IllegalAccessException {
        throw new IllegalAccessException(GlobalExceptionHandlerTest.DEFAULT_EXCEPTION_MESSAGE);
    }

    @GetMapping("someException")
    public void throwSomeRuntimeException() throws Exception {
        throw new Exception(GlobalExceptionHandlerTest.DEFAULT_EXCEPTION_MESSAGE);
    }
}
