package com.senkiv.carsharing.controller;

import com.senkiv.carsharing.dto.RentalRequestDto;
import com.senkiv.carsharing.dto.RentalResponseDto;
import com.senkiv.carsharing.dto.RentalReturnRequestDto;
import com.senkiv.carsharing.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponseDto addRental(@RequestBody @Valid RentalRequestDto requestDto) {
        return rentalService.add(requestDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<RentalResponseDto> getRentals(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "user_id", required = false) Long userId,
            @RequestParam(name = "is_active", defaultValue = "false") boolean isActive) {
        return rentalService.getByUserAndStatus(pageable, userId, isActive);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public RentalResponseDto getRentalById(@PathVariable Long id) {
        return rentalService.getById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/return")
    public RentalResponseDto returnRental(
            @PathVariable Long id,
            @RequestBody @Valid RentalReturnRequestDto requestDto) {
        return rentalService.returnRental(id, requestDto);
    }
}
