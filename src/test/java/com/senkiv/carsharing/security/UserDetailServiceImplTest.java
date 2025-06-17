package com.senkiv.carsharing.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.senkiv.carsharing.model.User;
import com.senkiv.carsharing.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String USER_NOT_FOUND_MESSAGE = "User with email %s does not exist";

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername(TEST_EMAIL);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    void loadUserByUsername_WithNonExistingUser_ShouldThrowException() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailService.loadUserByUsername(TEST_EMAIL)
        );

        assertEquals(USER_NOT_FOUND_MESSAGE.formatted(TEST_EMAIL), exception.getMessage());
        verify(userRepository).findByEmail(TEST_EMAIL);
    }
}
