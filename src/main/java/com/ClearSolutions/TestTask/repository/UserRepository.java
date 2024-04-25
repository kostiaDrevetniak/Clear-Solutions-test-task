package com.ClearSolutions.TestTask.repository;


import com.ClearSolutions.TestTask.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user WHERE user.birthDate BETWEEN ?1 AND ?2")
    List<User> findByBirthDateRange(LocalDate from, LocalDate to);
}
