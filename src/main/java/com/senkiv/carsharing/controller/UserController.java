package com.senkiv.carsharing.controller;

import com.senkiv.carsharing.dto.UserProfileUpdateRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.dto.UserRoleUpdateRequestDto;
import com.senkiv.carsharing.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('MANAGER')")
    public UserResponseDto updateRole(@PathVariable Long id,
            @RequestBody @Valid UserRoleUpdateRequestDto requestDto) {
        return userService.updateRole(id, requestDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserResponseDto getMyProfile() {
        return userService.getCurrentUser();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public UserResponseDto updateProfile(
            @RequestBody @Valid UserProfileUpdateRequestDto requestDto) {
        return userService.updateProfile(requestDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    public UserResponseDto patchProfile(
            @RequestBody @Valid UserProfileUpdateRequestDto requestDto) {
        return userService.updateProfile(requestDto);
    }
}
