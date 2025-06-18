package com.senkiv.carsharing.service;

import com.senkiv.carsharing.dto.RentalRequestDto;
import com.senkiv.carsharing.dto.RentalResponseDto;
import com.senkiv.carsharing.dto.RentalReturnRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalResponseDto add(RentalRequestDto requestDto);

    Page<RentalResponseDto> getByUserAndStatus(Pageable pageable, Long userId, boolean isActive);

    RentalResponseDto getById(Long id);

    RentalResponseDto returnRental(Long id, RentalReturnRequestDto requestDto);
}
