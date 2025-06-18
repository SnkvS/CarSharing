package com.senkiv.carsharing.service.impl;

import com.senkiv.carsharing.dto.RentalRequestDto;
import com.senkiv.carsharing.dto.RentalResponseDto;
import com.senkiv.carsharing.dto.RentalReturnRequestDto;
import com.senkiv.carsharing.exception.RentalException;
import com.senkiv.carsharing.mapper.RentalMapper;
import com.senkiv.carsharing.model.Car;
import com.senkiv.carsharing.model.Rental;
import com.senkiv.carsharing.model.User;
import com.senkiv.carsharing.repository.CarRepository;
import com.senkiv.carsharing.repository.RentalRepository;
import com.senkiv.carsharing.repository.UserRepository;
import com.senkiv.carsharing.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    public static final String UNAUTHORIZED_USER_MESSAGE = "User not authenticated";
    private static final String RENTAL_NOT_FOUND_MESSAGE = "Rental with id %d not found";
    private static final String CAR_NOT_FOUND_MESSAGE = "Car with id %d not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";
    private static final String CURRENT_USER_NOT_FOUND_MESSAGE = "Current user not found";
    private static final String CAR_NOT_AVAILABLE_MESSAGE = "Car with id %d is not available";
    private static final String RENTAL_ALREADY_RETURNED_MESSAGE =
            "Rental with id %d is already returned";
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;

    @Override
    @Transactional
    public RentalResponseDto add(RentalRequestDto requestDto) {
        Car car = carRepository.findById(requestDto.carId())
                .orElseThrow(() -> new EntityNotFoundException(
                        CAR_NOT_FOUND_MESSAGE.formatted(requestDto.carId())));
        if (car.getInventory() <= 0) {
            throw new RentalException(CAR_NOT_AVAILABLE_MESSAGE.formatted(car.getId()));
        }
        User user = getCurrentUser();
        Rental rental = rentalMapper.toModel(requestDto);
        rental.setCar(car);
        rental.setUser(user);
        car.setInventory(car.getInventory() - 1);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public Page<RentalResponseDto> getByUserAndStatus(Pageable pageable, Long userId,
            boolean isActive) {
        User user = userId == null ? getCurrentUser() :
                userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                USER_NOT_FOUND_MESSAGE.formatted(userId)));
        Page<Rental> rentals;
        if (isActive) {
            rentals = rentalRepository.findByUserAndActualReturnDateIsNull(pageable, user);
        } else {
            rentals = rentalRepository.findByUserAndActualReturnDateIsNotNull(pageable, user);
        }
        return rentals.map(rentalMapper::toDto);
    }

    @Override
    public RentalResponseDto getById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        RENTAL_NOT_FOUND_MESSAGE.formatted(id)));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalResponseDto returnRental(Long id, RentalReturnRequestDto requestDto) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        RENTAL_NOT_FOUND_MESSAGE.formatted(id)));
        if (rental.getActualReturnDate() != null) {
            throw new RentalException(RENTAL_ALREADY_RETURNED_MESSAGE.formatted(id));
        }
        rental.setActualReturnDate(requestDto.actualReturnDate());
        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException(UNAUTHORIZED_USER_MESSAGE);
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(CURRENT_USER_NOT_FOUND_MESSAGE));
    }
}
