package com.senkiv.carsharing.mapper;

import com.senkiv.carsharing.config.MapperConfig;
import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    Car toModel(CarRequestDto dto);

    CarResponseDto toDto(Car car);

    Car update(CarRequestDto carRequestDto, @MappingTarget Car car);
}
