package com.ClearSolutions.TestTask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "identify")
    private Long id;

    @Email(message = "Must be a valid e-mail address")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Past(message = "Must be earlier that current date")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Pattern(regexp = "^(?=\\s*\\S).*$", message = "The 'address' cannot be blank")
    private String address;

    @Pattern(regexp = "^(\\d{10}|(?:\\d{3}[- ]){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4})$",
            message = "Must be a valid phone number")
    private String phoneNumber;

}
