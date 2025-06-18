package com.senkiv.carsharing.dto;

import java.time.LocalDate;

public record RentalResponseDto(
        Long id,
        Long carId,
        Long userId,
        String userEmail,
        LocalDate rentalDate,
        LocalDate returnDate,
        LocalDate actualReturnDate,
        boolean isActive
) {
}
