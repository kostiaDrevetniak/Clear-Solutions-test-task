package com.ClearSolutions.TestTask.dto.request;

import com.ClearSolutions.TestTask.validation.anotation.ValidAge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString()
public class UserRequest {
    @NotBlank(message = "The 'email' cannot be blank")
    private String email;
    @NotBlank(message = "The 'firstName' cannot be blank")
    private String firstName;
    @NotBlank(message = "The 'lastName' cannot be blank")
    private String lastName;
    @ValidAge
    @NotNull(message = "The 'birthDate' cannot be null")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
