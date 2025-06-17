package com.senkiv.carsharing.service.impl;

import com.senkiv.carsharing.dto.UserLoginRequestDto;
import com.senkiv.carsharing.dto.UserLoginResponseDto;
import com.senkiv.carsharing.security.JwtUtil;
import com.senkiv.carsharing.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto dto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
