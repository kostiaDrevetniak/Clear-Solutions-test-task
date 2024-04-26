package com.ClearSolutions.TestTask.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionThrowingController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    final static String DEFAULT_EXCEPTION_MESSAGE = "Some message";

    @Test
    public void handleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test/methodArgumentNotValidException")
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
    public void handleConstraintViolationException() throws Exception {

        mockMvc.perform(post("/test/constraintViolationException")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"invalid email",
                            "firstName":"1",
                            "lastName":"   ",
                            "birthDate": "2040-05-16",
                            "address":"",
                            "phoneNumber":"380465857456481"
                        }\s"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("""
                        Error occurred:
                        \tMust be a valid e-mail address
                        \tMust be a valid phone number
                        \tMust be earlier that current date
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tMust start with a capital letter followed by one or more lowercase letters
                        \tThe 'address' cannot be blank
                        """));

    }

    @Test
    public void handleNullEntityReferenceException() throws Exception {
        mockMvc.perform(get("/test/nullEntityReferenceException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DEFAULT_EXCEPTION_MESSAGE));
    }

    @Test
    public void handleEntityNotFoundException() throws Exception {
        mockMvc.perform(get("/test/entityNotFoundException"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(DEFAULT_EXCEPTION_MESSAGE));
    }

    @Test
    public void handleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegalArgumentException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DEFAULT_EXCEPTION_MESSAGE));
    }

    @Test
    public void handleIllegalAccessException() throws Exception {
        mockMvc.perform(get("/test/illegalAccessException"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("problem occurred during patching entity"));
    }

    @Test
    public void handleSomeException() throws Exception {
        mockMvc.perform(get("/test/someException"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(DEFAULT_EXCEPTION_MESSAGE));
    }
}