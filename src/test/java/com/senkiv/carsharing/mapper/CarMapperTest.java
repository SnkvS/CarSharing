package com.senkiv.carsharing.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.model.Car;
import com.senkiv.carsharing.model.CarType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CarMapperTest {

    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    void toModel_ShouldMapDtoToEntity() {
        CarRequestDto requestDto = new CarRequestDto(
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        Car car = carMapper.toModel(requestDto);

        assertNotNull(car);
        assertEquals(requestDto.model(), car.getModel());
        assertEquals(requestDto.brand(), car.getBrand());
        assertEquals(requestDto.type(), car.getType());
        assertEquals(requestDto.inventory(), car.getInventory());
        assertEquals(requestDto.dailyFee(), car.getDailyFee());
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("Model 3");
        car.setBrand("Tesla");
        car.setType(CarType.SEDAN);
        car.setInventory(5);
        car.setDailyFee(BigDecimal.valueOf(100.00));

        CarResponseDto responseDto = carMapper.toDto(car);

        assertNotNull(responseDto);
        assertEquals(car.getId(), responseDto.id());
        assertEquals(car.getModel(), responseDto.model());
        assertEquals(car.getBrand(), responseDto.brand());
        assertEquals(car.getType(), responseDto.type());
        assertEquals(car.getInventory(), responseDto.inventory());
        assertEquals(car.getDailyFee(), responseDto.dailyFee());
    }
}
