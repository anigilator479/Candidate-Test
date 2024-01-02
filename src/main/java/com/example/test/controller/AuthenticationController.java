package com.example.test.controller;

import com.example.test.dto.user.UserAuthenticationRequestDto;
import com.example.test.dto.user.UserAuthenticationResponseDto;
import com.example.test.dto.user.UserRegistrationRequestDto;
import com.example.test.dto.user.UserRegistrationResponseDto;
import com.example.test.security.AuthenticationService;
import com.example.test.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Login", description = "Users can login using their credentials")
    @PostMapping("/authenticate")
    public UserAuthenticationResponseDto login(@RequestBody @Valid
                                               UserAuthenticationRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @Operation(summary = "Register", description = "Users can create new account")
    @PostMapping("/add")
    public UserRegistrationResponseDto register(@RequestBody @Valid
                                                    UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
