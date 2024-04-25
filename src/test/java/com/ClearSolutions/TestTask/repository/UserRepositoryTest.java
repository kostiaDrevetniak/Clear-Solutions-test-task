package com.ClearSolutions.TestTask.repository;

import com.ClearSolutions.TestTask.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("TestUser@mail.com");
        user.setFirstName("User");
        user.setLastName("Test");
        user.setAddress("Test address #1");
        user.setBirthDate(LocalDate.of(2000, 5, 16));
        user.setPhoneNumber("035 126 3491");
    }

    @Test
    public void getByBirthDateRange() {
        User expected = userRepository.save(this.user);


        List<User> actual = userRepository.findByBirthDateRange(LocalDate.of(2000, 1, 1),
                LocalDate.of(2000, 12, 31));

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    public void getByBirthDateRangeNotExisted() {
        User expected = userRepository.save(this.user);

        List<User> actual = userRepository.findByBirthDateRange(LocalDate.of(2001, 1, 1),
                LocalDate.of(2001, 12, 31));

        assertEquals(0, actual.size());
    }
}
