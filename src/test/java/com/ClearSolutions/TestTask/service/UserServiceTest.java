package com.ClearSolutions.TestTask.service;

import com.ClearSolutions.TestTask.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;
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
    void create() {
        User actual = userService.create(user);

        assertEquals(user, actual);
        assertNotEquals(0, actual.getId());
    }

    @Test
    void readById() {
        User expected = userService.create(user);
        User actual = userService.readById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        User expected = userService.create(user);
        expected.setFirstName("Test");
        expected.setLastName("User");

        User updateUser = new User();
        updateUser.setId(expected.getId());
        updateUser.setFirstName("Test");
        updateUser.setLastName("User");
        user.setEmail("TestUser@mail.com");
        updateUser.setAddress("Test address #1");
        updateUser.setBirthDate(LocalDate.of(2000, 5, 16));
        updateUser.setPhoneNumber("035 126 3491");

        User actual = userService.update(updateUser);

        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        int expectedSize = 2;
        userService.create(user);
        User newUser = new User();
        newUser.setEmail("TestUser2@mail.com");
        newUser.setFirstName("User");
        newUser.setLastName("Test");
        newUser.setAddress("Test address #1");
        newUser.setBirthDate(LocalDate.of(2000, 5, 16));
        newUser.setPhoneNumber("035 126 3491");
        userService.create(newUser);
        List<User> actual = userService.getAll();

        assertEquals(expectedSize, actual.size());
    }

    @Test
    void delete() {
        User newUser = userService.create(user);
        userService.delete(newUser.getId());
        int expectedSize = 0;
        List<User> actual = userService.getAll();

        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void getByBirthDateRange() {
        User expected = userService.create(this.user);

        System.out.println(userService.getAll());

        List<User> actual = userService.getAllByDateRange(LocalDate.of(2000, 1, 1),
                LocalDate.of(2000, 12, 31));

        System.out.println(actual);

        assertEquals(expected, actual.get(0));
    }

    @Test
    public void getByBirthDateRangeNotExisted() {
        userService.create(this.user);

        List<User> actual = userService.getAllByDateRange(LocalDate.of(2001, 1, 1),
                LocalDate.of(2001, 12, 31));

        assertEquals(0, actual.size());
    }
}
