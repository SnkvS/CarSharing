package com.senkiv.carsharing.service;

import com.senkiv.carsharing.dto.UserProfileUpdateRequestDto;
import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.dto.UserRoleUpdateRequestDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto dto);

    UserResponseDto updateRole(Long userId, UserRoleUpdateRequestDto dto);

    UserResponseDto getCurrentUser();

    UserResponseDto updateProfile(UserProfileUpdateRequestDto dto);
}
