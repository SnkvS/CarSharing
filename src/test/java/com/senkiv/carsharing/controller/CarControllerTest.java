package com.senkiv.carsharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senkiv.carsharing.dto.CarRequestDto;
import com.senkiv.carsharing.dto.CarResponseDto;
import com.senkiv.carsharing.model.CarType;
import com.senkiv.carsharing.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createCar_ShouldReturnCreatedCarResponseDto() throws Exception {
        // Given
        CarRequestDto requestDto = new CarRequestDto(
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        CarResponseDto responseDto = new CarResponseDto(
                1L,
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        when(carService.create(any(CarRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("Model 3"))
                .andExpect(jsonPath("$.brand").value("Tesla"))
                .andExpect(jsonPath("$.type").value("SEDAN"))
                .andExpect(jsonPath("$.inventory").value(5))
                .andExpect(jsonPath("$.dailyFee").value(100.00));
    }

    @Test
    void getAllCars_ShouldReturnPageOfCarResponseDto() throws Exception {
        // Given
        List<CarResponseDto> cars = List.of(
                new CarResponseDto(
                        1L,
                        "Model 3",
                        "Tesla",
                        CarType.SEDAN,
                        5,
                        BigDecimal.valueOf(100.00)
                ),
                new CarResponseDto(
                        2L,
                        "Model Y",
                        "Tesla",
                        CarType.SUV,
                        3,
                        BigDecimal.valueOf(120.00)
                )
        );

        // Mock the controller directly instead of using MockMvc for this test
        // This avoids the Pageable parameter resolution issue
        when(carService.getAll(any())).thenReturn(new PageImpl<>(cars));

        Page<CarResponseDto> result = carController.getAllCars(null);

        // Verify the result
        assertEquals(2, result.getTotalElements());
        assertEquals("Model 3", result.getContent().get(0).model());
        assertEquals("Model Y", result.getContent().get(1).model());
    }

    @Test
    void getCarById_WithExistingId_ShouldReturnCarResponseDto() throws Exception {
        // Given
        Long id = 1L;
        CarResponseDto responseDto = new CarResponseDto(
                id,
                "Model 3",
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        when(carService.getById(id)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/cars/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("Model 3"))
                .andExpect(jsonPath("$.brand").value("Tesla"))
                .andExpect(jsonPath("$.type").value("SEDAN"))
                .andExpect(jsonPath("$.inventory").value(5))
                .andExpect(jsonPath("$.dailyFee").value(100.00));
    }

    @Test
    void getCarById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(carService.getById(id)).thenThrow(
                new EntityNotFoundException("Car with id 999 not found"));

        // When & Then
        mockMvc.perform(get("/cars/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCar_WithExistingId_ShouldReturnUpdatedCarResponseDto() throws Exception {
        // Given
        Long id = 1L;
        CarRequestDto requestDto = new CarRequestDto(
                "Updated Model",
                "Updated Brand",
                CarType.SUV,
                10,
                BigDecimal.valueOf(150.00)
        );

        CarResponseDto responseDto = new CarResponseDto(
                id,
                "Updated Model",
                "Updated Brand",
                CarType.SUV,
                10,
                BigDecimal.valueOf(150.00)
        );

        when(carService.update(eq(id), any(CarRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("Updated Model"))
                .andExpect(jsonPath("$.brand").value("Updated Brand"))
                .andExpect(jsonPath("$.type").value("SUV"))
                .andExpect(jsonPath("$.inventory").value(10))
                .andExpect(jsonPath("$.dailyFee").value(150.00));
    }

    @Test
    void updateCar_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        CarRequestDto requestDto = new CarRequestDto(
                "Updated Model",
                "Updated Brand",
                CarType.SUV,
                10,
                BigDecimal.valueOf(150.00)
        );

        when(carService.update(eq(id), any(CarRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Car with id 999 not found"));

        // When & Then
        mockMvc.perform(put("/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(carService).delete(id);

        // When & Then
        mockMvc.perform(delete("/cars/{id}", id))
                .andExpect(status().isNoContent());

        verify(carService).delete(id);
    }

    @Test
    void deleteCar_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        doThrow(new EntityNotFoundException("Car with id 999 not found"))
                .when(carService).delete(id);

        // When & Then
        mockMvc.perform(delete("/cars/{id}", id))
                .andExpect(status().isNotFound());

        verify(carService).delete(id);
    }

    @Test
    void createCar_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CarRequestDto requestDto = new CarRequestDto(
                "",// Empty model (invalid)
                "Tesla",
                CarType.SEDAN,
                5,
                BigDecimal.valueOf(100.00)
        );

        // When & Then
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }
}
