package com.ClearSolutions.TestTask.service;

import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.service.impl.UserPatcherImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserPatcherTest {
    @Autowired
    Patcher<User> patcher;
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
    public void TestUserPatching() {
        User newUser = new User();
        newUser.setFirstName("Test");
        newUser.setLastName("User");

        patcher.patch(user, newUser);

        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
    }
}
