package com.example.test.security;

import com.example.test.dto.user.UserAuthenticationRequestDto;
import com.example.test.dto.user.UserAuthenticationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserAuthenticationResponseDto authenticate(UserAuthenticationRequestDto requestDto) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        requestDto.username(), requestDto.password()));
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserAuthenticationResponseDto(token);
    }
}
