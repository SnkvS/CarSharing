package com.senkiv.carsharing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.mapper.CarMapper;
import com.senkiv.carsharing.model.Car;
import com.senkiv.carsharing.model.CarType;
import com.senkiv.carsharing.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void create_ShouldReturnCarResponseDto() {
        CarRequestDto requestDto = new CarRequestDto(
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );
        Car car = new Car();
        car.setId(1L);
        car.setModel(requestDto.model());
        car.setBrand(requestDto.brand());
        car.setType(requestDto.type());
        car.setInventory(requestDto.inventory());
        car.setDailyFee(requestDto.dailyFee());

        CarResponseDto expectedDto = new CarResponseDto(
                1L,
                requestDto.model(),
                requestDto.brand(),
                requestDto.type(),
                requestDto.inventory(),
                requestDto.dailyFee()
        );

        when(carMapper.toModel(requestDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(expectedDto);

        CarResponseDto actualDto = carService.create(requestDto);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(carMapper).toModel(requestDto);
        verify(carRepository).save(car);
        verify(carMapper).toDto(car);
    }

    @Test
    void getById_WithExistingId_ShouldReturnCarResponseDto() {
        Long id = 1L;
        Car car = new Car();
        car.setId(id);
        car.setModel("Model 3");
        car.setBrand("Tesla");
        car.setType(CarType.SEDAN);
        car.setInventory(5);
        car.setDailyFee(BigDecimal.valueOf(100.00));

        CarResponseDto expectedDto = new CarResponseDto(
                id,
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(expectedDto);

        CarResponseDto actualDto = carService.getById(id);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(carRepository).findById(id);
        verify(carMapper).toDto(car);
    }

    @Test
    void getById_WithNonExistingId_ShouldThrowException() {
        Long id = 999L;
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.getById(id)
        );

        assertEquals("Car with id 999 not found", exception.getMessage());
        verify(carRepository).findById(id);
        verifyNoInteractions(carMapper);
    }

    @Test
    void update_WithExistingId_ShouldReturnUpdatedCarResponseDto() {
        Long id = 1L;
        final CarRequestDto requestDto = new CarRequestDto(
                "Updated Model",
                "Updated Brand",
                CarType.SUV,
                10,
                BigDecimal.valueOf(150.00)
        );

        Car existingCar = new Car();
        existingCar.setId(id);
        existingCar.setModel("Model 3");
        existingCar.setBrand("Tesla");
        existingCar.setType(CarType.SEDAN);
        existingCar.setInventory(5);
        existingCar.setDailyFee(BigDecimal.valueOf(100.00));

        Car updatedCar = new Car();
        updatedCar.setId(id);
        updatedCar.setModel(requestDto.model());
        updatedCar.setBrand(requestDto.brand());
        updatedCar.setType(requestDto.type());
        updatedCar.setInventory(requestDto.inventory());
        updatedCar.setDailyFee(requestDto.dailyFee());

        CarResponseDto expectedDto = new CarResponseDto(
                id,
                requestDto.model(),
                requestDto.brand(),
                requestDto.type(),
                requestDto.inventory(),
                requestDto.dailyFee()
        );

        when(carRepository.findById(id)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenReturn(updatedCar);
        when(carMapper.toDto(updatedCar)).thenReturn(expectedDto);

        CarResponseDto actualDto = carService.update(id, requestDto);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(carRepository).findById(id);
        verify(carRepository).save(any(Car.class));
        verify(carMapper).toDto(updatedCar);
    }

    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        Long id = 999L;
        CarRequestDto requestDto = new CarRequestDto(
                "Updated Model",
                "Updated Brand",
                CarType.SUV,
                10,
                BigDecimal.valueOf(150.00)
        );

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.update(id, requestDto)
        );

        assertEquals("Car with id 999 not found", exception.getMessage());
        verify(carRepository).findById(id);
        verifyNoMoreInteractions(carRepository);
        verifyNoInteractions(carMapper);
    }

    @Test
    void delete_WithExistingId_ShouldDeleteCar() {
        Long id = 1L;
        Car car = new Car();
        car.setId(id);

        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).deleteById(id);

        carService.delete(id);

        verify(carRepository).findById(id);
        verify(carRepository).deleteById(id);
    }

    @Test
    void delete_WithNonExistingId_ShouldThrowException() {
        Long id = 999L;
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.delete(id)
        );

        assertEquals("Car with id 999 not found", exception.getMessage());
        verify(carRepository).findById(id);
        verifyNoMoreInteractions(carRepository);
    }
}
