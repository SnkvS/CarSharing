package com.senkiv.carsharing.service;

import com.senkiv.carsharing.dto.UserLoginRequestDto;
import com.senkiv.carsharing.dto.UserLoginResponseDto;

public interface AuthenticationService {

    UserLoginResponseDto authenticate(UserLoginRequestDto dto);
}
