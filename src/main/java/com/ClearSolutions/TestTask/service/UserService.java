package com.ClearSolutions.TestTask.service;

import com.ClearSolutions.TestTask.Model.User;
import com.ClearSolutions.TestTask.controler.UserController;
import com.ClearSolutions.TestTask.repository.UserRepository;
import org.springframework.stereotype.Service;

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
