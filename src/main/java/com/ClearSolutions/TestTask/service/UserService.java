package com.ClearSolutions.TestTask.service;

import com.ClearSolutions.TestTask.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User create(User user);

    User readById(long id);

    User update(User user);

    void delete(long id);

    List<User> getAll();

    List<User> getAllByDateRange(LocalDate from, LocalDate to);
}
