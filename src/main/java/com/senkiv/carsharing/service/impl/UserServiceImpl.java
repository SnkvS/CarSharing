package com.senkiv.carsharing.service.impl;

import com.senkiv.carsharing.dto.UserProfileUpdateRequestDto;
import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import com.senkiv.carsharing.dto.UserResponseDto;
import com.senkiv.carsharing.dto.UserRoleUpdateRequestDto;
import com.senkiv.carsharing.exception.RegistrationException;
import com.senkiv.carsharing.mapper.UserMapper;
import com.senkiv.carsharing.model.Role;
import com.senkiv.carsharing.model.Role.RoleName;
import com.senkiv.carsharing.model.User;
import com.senkiv.carsharing.repository.RoleRepository;
import com.senkiv.carsharing.repository.UserRepository;
import com.senkiv.carsharing.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String EMAIL_ALREADY_USED_MESSAGE = "Email %s is already used.";
    public static final String ERROR_DEFAULT_ROLE_ASSIGNMENT =
            "Cannot assign default role to user.";
    public static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role %s not found";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto dto) {
        User user = userMapper.toModel(dto);
        if (userRepository.existsByEmail(user.getUsername())) {
            throw new RegistrationException(
                    EMAIL_ALREADY_USED_MESSAGE.formatted(user.getUsername()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRoleName(RoleName.CUSTOMER)
                .orElseThrow(() -> new EntityNotFoundException(
                        ERROR_DEFAULT_ROLE_ASSIGNMENT))));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public UserResponseDto updateRole(Long userId, UserRoleUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        USER_NOT_FOUND_MESSAGE.formatted(userId)));

        Role role = roleRepository.findByRoleName(dto.roleName())
                .orElseThrow(() -> new EntityNotFoundException(
                        ROLE_NOT_FOUND_MESSAGE.formatted(dto.roleName())));

        user.setRoles(Set.of(role));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateProfile(UserProfileUpdateRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));

        // Check if email is being changed and if it's already in use
        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new RegistrationException(EMAIL_ALREADY_USED_MESSAGE.formatted(dto.email()));
        }

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());

        return userMapper.toDto(userRepository.save(user));
    }
}
