package com.senkiv.carsharing.dto;

import com.senkiv.carsharing.model.Role.RoleName;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequestDto(
        @NotNull
        RoleName roleName
) {
}
