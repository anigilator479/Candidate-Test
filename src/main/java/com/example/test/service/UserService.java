package com.example.test.service;

import com.example.test.dto.user.UserRegistrationRequestDto;
import com.example.test.dto.user.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto);
}
