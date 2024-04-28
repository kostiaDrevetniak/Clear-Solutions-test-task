package com.ClearSolutions.TestTask.integration;

import com.ClearSolutions.TestTask.TestTaskApplication;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TestTaskApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
@Transactional
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    public void setUp() {
        user = new User();

        user.setEmail("TestUser@mail.com");
        user.setFirstName("User");
        user.setLastName("Test");
        user.setAddress("Test address #1");
        user.setBirthDate(LocalDate.of(2005, 5, 16));
        user.setPhoneNumber("035 126 3491");
    }

    @Test
    public void createValidUser() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"TestUser@mail.com",
                            "firstName":"User",
                            "lastName":"Test",
                            "birthDate": "2005-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void createUserNotValidRequestData() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"   ",
                            "firstName":"",
                            "lastName":null,
                            "birthDate": "2015-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].name").value("birthDate"))
                .andExpect(jsonPath("$.[0].message").value("Age must be greater that 18"))
                .andExpect(jsonPath("$.[1].name").value("email"))
                .andExpect(jsonPath("$.[1].message").value("The 'email' cannot be blank"))
                .andExpect(jsonPath("$.[2].name").value("firstName"))
                .andExpect(jsonPath("$.[2].message").value("The 'firstName' cannot be blank"))
                .andExpect(jsonPath("$.[3].name").value("lastName"))
                .andExpect(jsonPath("$.[3].message").value("The 'lastName' cannot be blank"));
    }

    @Test
    public void createNotValidUserRequest() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"invalid email",
                            "firstName":"1",
                            "lastName":"#$%!@#$#@!",
                            "birthDate": "2004-05-16",
                            "address":"",
                            "phoneNumber":"380465857456481"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("""
                        Error occurred:
                        \tMust be a valid e-mail address
                        \tMust be a valid phone number
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tThe 'address' cannot be blank
                        """));

    }

    @Test
    public void getExistedUser() throws Exception {
        userRepository.save(user);

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void getNotExistedUser() throws Exception {
        int id = 8;

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("User with id %d not found", id)));
    }

    @Test
    public void getAllUsers() throws Exception {
        userRepository.save(user);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(user.getId()))
                .andExpect(jsonPath("$.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[0].birthDate").value(user.getBirthDate().toString()))
                .andExpect(jsonPath("$.[0].address").value(user.getAddress()))
                .andExpect(jsonPath("$.[0].phoneNumber").value(user.getPhoneNumber()));

    }

    @Test
    public void deleteExistedUser() throws Exception {
        userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isOk());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void deleteNotExistedUser() throws Exception {
        int id = 8;

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("User with id %d not found", id)));
    }

    @Test
    public void getUsersByBirthDateRange() throws Exception {
        userRepository.save(user);
        LocalDate start = LocalDate.of(2005, 1, 1);
        LocalDate end = LocalDate.of(2005, 12, 31);

        mockMvc.perform(get("/api/users/birthDate")
                .param("from", start.toString())
                .param("to", end.toString())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(user.getId()))
                .andExpect(jsonPath("$.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[0].birthDate").value(user.getBirthDate().toString()))
                .andExpect(jsonPath("$.[0].address").value(user.getAddress()))
                .andExpect(jsonPath("$.[0].phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void getUsersByBirthDateRangeFromGreaterThanTo() throws Exception {
        userRepository.save(user);
        LocalDate start = LocalDate.of(2005, 12, 31);
        LocalDate end = LocalDate.of(2005, 1, 1);

        mockMvc.perform(get("/api/users/birthDate")
                .param("from", start.toString())
                .param("to", end.toString())
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Birth date range start must be smaller than the end"));
    }

    @Test
    public void updateValidUser() throws Exception {
        userRepository.save(user);

        user.setLastName("User");
        user.setFirstName("Test");

        mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "email":"TestUser@mail.com",
                            "firstName":"Test",
                            "lastName":"User",
                            "birthDate": "2005-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isOk());

        Optional<User> optionalUser = userRepository.findById(user.getId());

        assertTrue(optionalUser.isPresent());
        User actualUser = optionalUser.get();
        assertEquals(user.getLastName(), actualUser.getLastName());
        assertEquals(user.getFirstName(), actualUser.getFirstName());

    }

    @Test
    public void updateNotExistedUser() throws Exception {
        int id = 8;

        mockMvc.perform(put("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "email":"TestUser@mail.com",
                            "firstName":"Test",
                            "lastName":"User",
                            "birthDate": "2005-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("User with id %d not found", id)));
    }

    @Test
    public void updateUserNotValidRequestData() throws Exception {
        userRepository.save(user);

        mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"   ",
                            "firstName":"",
                            "lastName":null,
                            "birthDate": "2015-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].name").value("birthDate"))
                .andExpect(jsonPath("$.[0].message").value("Age must be greater that 18"))
                .andExpect(jsonPath("$.[1].name").value("email"))
                .andExpect(jsonPath("$.[1].message").value("The 'email' cannot be blank"))
                .andExpect(jsonPath("$.[2].name").value("firstName"))
                .andExpect(jsonPath("$.[2].message").value("The 'firstName' cannot be blank"))
                .andExpect(jsonPath("$.[3].name").value("lastName"))
                .andExpect(jsonPath("$.[3].message").value("The 'lastName' cannot be blank"));
    }

    @Test
    public void updateNotValidUser() throws Exception {
        userRepository.save(user);

        mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"invalid email",
                            "firstName":"1",
                            "lastName":"#$%!@#$#@!",
                            "birthDate": "2004-05-16",
                            "address":"",
                            "phoneNumber":"380465857456481"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("""
                        Error occurred:
                        \tMust be a valid e-mail address
                        \tMust be a valid phone number
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tThe 'address' cannot be blank
                        """));
    }

    @Test
    public void patchValidUser() throws Exception {
        userRepository.save(user);

        user.setLastName("User");
        user.setFirstName("Test");

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "firstName":"Test",
                            "lastName":"User"
                        }\s"""))
                .andExpect(status().isOk());

        Optional<User> optionalUser = userRepository.findById(user.getId());

        assertTrue(optionalUser.isPresent());
        User actualUser = optionalUser.get();
        assertEquals(user.getLastName(), actualUser.getLastName());
        assertEquals(user.getFirstName(), actualUser.getFirstName());
    }

    @Test
    public void patchNotExistedUser() throws Exception {
        int id = 8;

        mockMvc.perform(patch("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "email":"TestUser@mail.com"
                        }\s"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("User with id %d not found", id)));
    }

    @Test
    public void patchNotValidUser() throws Exception {
        userRepository.save(user);

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"invalid email",
                            "firstName":"1",
                            "lastName":"#$%!@#$#@!",
                            "address":"",
                            "phoneNumber":"380465857456481"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("""
                        Error occurred:
                        \tMust be a valid e-mail address
                        \tMust be a valid phone number
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tThe 'address' cannot be blank
                        """));
    }
}
