package com.senkiv.carsharing.dto;

import com.senkiv.carsharing.model.Role.RoleName;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Set<RoleName> roles
) {
}
