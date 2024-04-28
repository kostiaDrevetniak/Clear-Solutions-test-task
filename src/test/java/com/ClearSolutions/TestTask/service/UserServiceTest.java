package com.ClearSolutions.TestTask.service;

import com.ClearSolutions.TestTask.exception.NullEntityReferenceException;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("TestUser@mail.com");
        user.setFirstName("User");
        user.setLastName("Test");
        user.setAddress("Test address #1");
        user.setBirthDate(LocalDate.of(2000, 5, 16));
        user.setPhoneNumber("035 126 3491");
    }

    @Test
    public void createValidUser() {
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User actual = userService.create(user);

        assertEquals(user, actual);
        assertNotEquals(0, actual.getId());
    }

    @Test
    public void crateNullUser() {
        assertThrows(NullEntityReferenceException.class,
                () -> userService.create(null));
    }

    @Test
    public void createNotValidUser() {
        User invalidUser = new User();
        invalidUser.setEmail("invalid email");
        invalidUser.setFirstName("1");
        invalidUser.setLastName("   \t\n");
        invalidUser.setAddress("");
        invalidUser.setBirthDate(LocalDate.now().plusMonths(6));
        invalidUser.setPhoneNumber("380465857456481");

        assertThrows(ConstraintViolationException.class, () -> userService.create(invalidUser));
        try {
            userService.create(invalidUser);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            assertEquals(6, constraintViolations.size());
        }
    }

    @Test
    public void readByExistedId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        User actual = userService.readById(1);

        assertEquals(user, actual);
    }

    @Test
    public void readByNotExistedId() {
        assertThrows(EntityNotFoundException.class, () -> userService.readById(5));
    }

    @Test
    public void updateExistedUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        user.setFirstName("Test");
        user.setLastName("User");

        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setFirstName("Test");
        updateUser.setLastName("User");
        updateUser.setEmail("TestUser@mail.com");
        updateUser.setAddress("Test address #1");
        updateUser.setBirthDate(LocalDate.of(2000, 5, 16));
        updateUser.setPhoneNumber("035 126 3491");
        Mockito.when(userRepository.save(updateUser)).thenReturn(updateUser);

        User actual = userService.update(updateUser);

        assertEquals(user, actual);
    }

    @Test
    public void updateNullUser() {
        assertThrows(NullEntityReferenceException.class,
                () -> userService.update(null));
    }

    @Test
    public void updateNotValidUser() {
        User invalidUser = new User();
        invalidUser.setEmail("invalid email");
        invalidUser.setFirstName("1");
        invalidUser.setLastName("   \t\n");
        invalidUser.setAddress("");
        invalidUser.setBirthDate(LocalDate.now().plusMonths(6));
        invalidUser.setPhoneNumber("380465857456481");

        assertThrows(ConstraintViolationException.class, () -> userService.update(invalidUser));
        try {
            userService.update(invalidUser);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            assertEquals(6, constraintViolations.size());
        }
    }

    @Test
    public void getAllUsers() {
        int expectedSize = 2;
        Mockito.when(userRepository.findAll()).thenReturn(List.of(
                user, user
        ));

        List<User> actual = userService.getAll();

        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void deleteUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findAll()).thenReturn(new LinkedList<>());

        userService.delete(user.getId());
        int expectedSize = 0;
        List<User> actual = userService.getAll();

        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void getByBirthDateRange() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2000, 12, 31);
        Mockito.when(userRepository.findByBirthDateRange(start, end)).thenReturn(List.of(user));


        System.out.println(userService.getAll());

        List<User> actual = userService.getAllByDateRange(start, end);

        assertEquals(List.of(user), actual);
    }

    @Test
    public void getByBirthDateRangeNotExisted() {
        LocalDate start = LocalDate.of(2001, 1, 1);
        LocalDate end = LocalDate.of(2001, 12, 31);
        Mockito.when(userRepository.findByBirthDateRange(start, end)).thenReturn(new LinkedList<>());

        List<User> actual = userService.getAllByDateRange(start, end);

        assertEquals(0, actual.size());
    }

    @Test
    public void getByBirthDateRangeWhereStartDateGreaterThatEnd() {
        LocalDate start = LocalDate.of(2001, 1, 1);
        LocalDate end = LocalDate.of(2001, 12, 31);

        assertThrows(IllegalArgumentException.class, () -> userService.getAllByDateRange(end, start));
    }
}
