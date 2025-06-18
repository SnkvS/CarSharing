package com.senkiv.carsharing.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RentalReturnRequestDto(
        @NotNull
        LocalDate actualReturnDate
) {
}
