package com.ClearSolutions.TestTask.controller;

import com.ClearSolutions.TestTask.controler.UserController;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.service.Patcher;
import com.ClearSolutions.TestTask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private Patcher<User> patcher;
    @Autowired
    private ModelMapper modelMapper;

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
        when(userService.create(any(User.class)))
                .thenAnswer(invocationOnMock -> {
                    user.setId(1L);
                    return user;
                });

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "email":"TestUser@mail.com",
                            "firstName":"User",
                            "lastName":"Test",
                            "birthDate": "2005-05-16",
                            "address":"Test address #1",
                            "phoneNumber":"035 126 3491"
                        }\s"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value("2005-05-16"))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void updateValidUser() throws Exception {
        user.setId(1L);
        when(userService.readById(1L)).thenReturn(user);
        user.setLastName("User");
        user.setFirstName("Test");

        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
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
    }

    @Test
    public void patchValidUser() throws Exception {
        user.setId(1L);
        when(userService.readById(1L)).thenReturn(user);

        user.setLastName("User");
        user.setFirstName("Test");
        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "firstName":"Test",
                            "lastName":"User"
                        }\s"""))
                .andExpect(status().isOk());
    }

    @Test
    public void getExistedUser() throws Exception {
        user.setId(1L);
        when(userService.readById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value("2005-05-16"))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void getAllUsers() throws Exception {
        user.setId(1L);
        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[0].birthDate").value("2005-05-16"))
                .andExpect(jsonPath("$.[0].address").value(user.getAddress()))
                .andExpect(jsonPath("$.[0].phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    public void getUsersByBirthDateRange() throws Exception {
        user.setId(1L);
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2000, 12, 31);
        when(userService.getAllByDateRange(eq(start), eq(end))).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users/birthDate")
                .param("from", "2000-01-01")
                .param("to", "2000-12-31")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[0].birthDate").value("2005-05-16"))
                .andExpect(jsonPath("$.[0].address").value(user.getAddress()))
                .andExpect(jsonPath("$.[0].phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    public void deleteExistedUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
