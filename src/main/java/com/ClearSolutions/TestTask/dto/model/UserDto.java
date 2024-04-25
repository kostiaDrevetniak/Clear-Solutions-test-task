package com.ClearSolutions.TestTask.dto.model;

import com.ClearSolutions.TestTask.validation.anotation.ValidAge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id"})
public class UserDto {
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @ValidAge
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
