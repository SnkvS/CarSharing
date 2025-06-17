package com.senkiv.carsharing.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senkiv.carsharing.dto.UserLoginRequestDto;
import com.senkiv.carsharing.dto.UserLoginResponseDto;
import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.model.Role;
import com.senkiv.carsharing.service.AuthenticationService;
import com.senkiv.carsharing.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "test.jwt.token";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .build();
    }

    @Test
    void register_ShouldReturnUserResponseDto() throws Exception {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
        );

        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "John",
                "Doe",
                TEST_EMAIL,
                Set.of(Role.RoleName.CUSTOMER)
        );

        when(userService.register(any(UserRegistrationRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.roles[0]").value("CUSTOMER"));
    }

    @Test
    void authenticate_ShouldReturnUserLoginResponseDto() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                TEST_EMAIL,
                TEST_PASSWORD
        );

        UserLoginResponseDto responseDto = new UserLoginResponseDto(TEST_TOKEN);

        when(authenticationService.authenticate(any(UserLoginRequestDto.class))).thenReturn(
                responseDto);

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TEST_TOKEN));
    }

    @Test
    void register_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "", // Empty firstName (invalid)
                "Doe",
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
        );

        // When & Then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "invalid-email", // Invalid email format
                TEST_PASSWORD
        );

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
