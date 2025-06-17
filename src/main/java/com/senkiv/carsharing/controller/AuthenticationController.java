package com.senkiv.carsharing.controller;

import com.senkiv.carsharing.dto.UserLoginRequestDto;
import com.senkiv.carsharing.dto.UserLoginResponseDto;
import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.service.AuthenticationService;
import com.senkiv.carsharing.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto authenticate(@RequestBody @Valid UserLoginRequestDto dto) {
        return authenticationService.authenticate(dto);
    }
}
