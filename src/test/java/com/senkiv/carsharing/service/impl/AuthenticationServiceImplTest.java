package com.senkiv.carsharing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.senkiv.carsharing.dto.UserLoginRequestDto;
import com.senkiv.carsharing.dto.UserLoginResponseDto;
import com.senkiv.carsharing.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_TOKEN = "test.jwt.token";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticate_ShouldReturnTokenWhenCredentialsAreValid() {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(TEST_EMAIL, TEST_PASSWORD);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(TEST_EMAIL, TEST_PASSWORD);

        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(jwtUtil.generateToken(TEST_EMAIL)).thenReturn(TEST_TOKEN);

        UserLoginResponseDto result = authenticationService.authenticate(loginRequestDto);

        assertNotNull(result);
        assertEquals(TEST_TOKEN, result.token());

        verify(authenticationManager).authenticate(authToken);
        verify(jwtUtil).generateToken(TEST_EMAIL);
    }

    @Test
    void authenticate_ShouldPropagateExceptionWhenAuthenticationFails() {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(TEST_EMAIL, TEST_PASSWORD);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(TEST_EMAIL, TEST_PASSWORD);

        when(authenticationManager.authenticate(authToken))
                .thenThrow(new RuntimeException("Authentication failed"));

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.authenticate(loginRequestDto)
        );

        assertEquals("Authentication failed", exception.getMessage());
        verify(authenticationManager).authenticate(authToken);
        verifyNoInteractions(jwtUtil);
    }
}
