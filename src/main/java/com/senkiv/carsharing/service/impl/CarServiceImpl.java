package com.senkiv.carsharing.service.impl;

import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.mapper.CarMapper;
import com.senkiv.carsharing.model.Car;
import com.senkiv.carsharing.repository.CarRepository;
import com.senkiv.carsharing.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private static final String CAR_NOT_FOUND_MESSAGE = "Car with id %d not found";

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    @Transactional
    public CarResponseDto create(CarRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public Page<CarResponseDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(carMapper::toDto);

    }

    @Override
    public CarResponseDto getById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        CAR_NOT_FOUND_MESSAGE.formatted(id)));
        return carMapper.toDto(car);
    }

    @Override
    @Transactional
    public CarResponseDto update(Long id, CarRequestDto requestDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        CAR_NOT_FOUND_MESSAGE.formatted(id)));

        carMapper.update(requestDto, car);

        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        CAR_NOT_FOUND_MESSAGE.formatted(id)));
        carRepository.deleteById(id);
    }
}
