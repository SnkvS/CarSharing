package com.senkiv.carsharing.service;

import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarResponseDto create(CarRequestDto requestDto);

    Page<CarResponseDto> getAll(Pageable pageable);

    CarResponseDto getById(Long id);

    CarResponseDto update(Long id, CarRequestDto requestDto);

    void delete(Long id);
}
