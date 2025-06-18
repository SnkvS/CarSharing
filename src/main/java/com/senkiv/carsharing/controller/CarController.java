package com.senkiv.carsharing.controller;

import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto createCar(@RequestBody @Valid CarRequestDto requestDto) {
        return carService.create(requestDto);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public Page<CarResponseDto> getAllCars(Pageable pageable) {
        return carService.getAll(pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable Long id) {
        return carService.getById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public CarResponseDto updateCar(@PathVariable Long id,
            @RequestBody @Valid CarRequestDto requestDto) {
        return carService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.delete(id);
    }
}
