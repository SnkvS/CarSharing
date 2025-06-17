package com.senkiv.carsharing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserProfileUpdateRequestDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotNull
        String email
) {
}
