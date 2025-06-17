package com.senkiv.carsharing.dto;

import com.senkiv.carsharing.model.CarType;
import java.math.BigDecimal;

public record CarResponseDto(
        Long id,
        String model,
        String brand,
        CarType type,
        Integer inventory,
        BigDecimal dailyFee
) {
}
