package com.ClearSolutions.TestTask.service.impl;

import com.ClearSolutions.TestTask.exception.NullEntityReferenceException;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.repository.UserRepository;
import com.ClearSolutions.TestTask.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public User create(User user) {
        if (user == null)
            throw new NullEntityReferenceException("User cannot be 'null'");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> constraintViolation : violations.stream()
                    .sorted(Comparator.comparing(ConstraintViolation::getMessage)).collect(Collectors.toList())) {
                sb.append("\t").append(constraintViolation.getMessage()).append("\n");
            }
            throw new ConstraintViolationException("Error occurred:\n" + sb.toString(), violations);
        }

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (user == null)
            throw new NullEntityReferenceException("User cannot be 'null'");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> constraintViolation : violations.stream()
                    .sorted(Comparator.comparing(ConstraintViolation::getMessage)).collect(Collectors.toList())) {
                sb.append("\t").append(constraintViolation.getMessage()).append("\n");
            }
            throw new ConstraintViolationException("Error occurred:\n" + sb.toString(), violations);
        }
        readById(user.getId());
        return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllByDateRange(LocalDate from, LocalDate to) {
        if (to.isBefore(from))
            throw new IllegalArgumentException("Birth date range start must be smaller than the end");
        return userRepository.findByBirthDateRange(from, to);
    }
}
