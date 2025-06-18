package com.senkiv.carsharing.dto;

import com.senkiv.carsharing.validation.annotation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@FieldMatch(first = "password", second = "repeatedPassword")
public record UserRegistrationRequestDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotNull
        String email,
        @NotBlank
        String password,
        @NotBlank
        String repeatedPassword
) {

}
