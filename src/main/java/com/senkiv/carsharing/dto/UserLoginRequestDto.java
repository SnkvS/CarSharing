package com.senkiv.carsharing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequestDto(
        @Email
        @NotNull
        String email,
        @NotBlank
        String password
) {
}
