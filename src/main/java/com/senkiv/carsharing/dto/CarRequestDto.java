package com.senkiv.carsharing.dto;

import com.senkiv.carsharing.model.CarType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CarRequestDto(
        @NotBlank
        String model,
        @NotBlank
        String brand,
        @NotNull
        CarType type,
        @NotNull
        @Min(0)
        Integer inventory,
        @NotNull
        @Positive
        BigDecimal dailyFee
) {
}
