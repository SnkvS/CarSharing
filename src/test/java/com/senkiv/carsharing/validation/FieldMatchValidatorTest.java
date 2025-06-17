package com.senkiv.carsharing.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.senkiv.carsharing.dto.UserRegistrationRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldMatchValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateWhenFieldsMatch() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                "test@example.com",
                "password123",
                "password123"
        );

        Set<ConstraintViolation<UserRegistrationRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No violations should be found when fields match");
    }

    @Test
    void shouldNotValidateWhenFieldsDoNotMatch() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                "test@example.com",
                "password123",
                "differentPassword"
        );

        Set<ConstraintViolation<UserRegistrationRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "One violation should be found when fields don't match");
        ConstraintViolation<UserRegistrationRequestDto> violation = violations.iterator().next();
        assertEquals(FieldMatch.FIELDS_ARE_NOT_MATCHING, violation.getMessage(),
                "Violation message should match the expected message");
    }

    @Test
    void shouldValidateWhenBothFieldsAreNull() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<UserRegistrationRequestDto>> violations = validator.validate(dto);

        boolean hasFieldMatchViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals(FieldMatch.FIELDS_ARE_NOT_MATCHING));
        assertFalse(hasFieldMatchViolation,
                "No FieldMatch violation should be found when both fields are null");
    }

    @Test
    void shouldValidateWhenBothFieldsAreEmpty() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                "test@example.com",
                "",
                ""
        );

        Set<ConstraintViolation<UserRegistrationRequestDto>> violations = validator.validate(dto);

        boolean hasFieldMatchViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals(FieldMatch.FIELDS_ARE_NOT_MATCHING));
        assertFalse(hasFieldMatchViolation,
                "No FieldMatch violation should be found when both fields are empty");
    }

    @Test
    void shouldNotValidateWhenOneFieldIsNull() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "Doe",
                "test@example.com",
                "password123",
                null
        );

        Set<ConstraintViolation<UserRegistrationRequestDto>> violations = validator.validate(dto);

        boolean hasFieldMatchViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals(FieldMatch.FIELDS_ARE_NOT_MATCHING));
        assertTrue(hasFieldMatchViolation,
                "FieldMatch violation should be found when one field is null");
    }
}
