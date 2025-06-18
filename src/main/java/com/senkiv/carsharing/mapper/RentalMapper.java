package com.senkiv.carsharing.mapper;

import com.senkiv.carsharing.config.MapperConfig;
import com.senkiv.carsharing.dto.RentalRequestDto;
import com.senkiv.carsharing.dto.RentalResponseDto;
import com.senkiv.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "car.id", source = "carId")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Rental toModel(RentalRequestDto dto);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "isActive", expression = "java(rental.getActualReturnDate() == null)")
    RentalResponseDto toDto(Rental rental);
}
