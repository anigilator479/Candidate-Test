package com.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.test.dto.user.UserRegistrationRequestDto;
import com.example.test.dto.user.UserRegistrationResponseDto;
import com.example.test.exception.RegistrationException;
import com.example.test.mapper.user.UserMapper;
import com.example.test.mapper.user.impl.UserMapperImpl;
import com.example.test.model.User;
import com.example.test.repository.UserRepository;
import com.example.test.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Throw exception by trying to register existing user")
    void registerUser_userAlreadyExists_throwRegistrationException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("test",
                "q1234");

        when(userRepository.existsByUsername(requestDto.username())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Register new user with valid register data")
    void registerUser_WithValidRegisterData_Ok() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("test",
                "q1234");

        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode("q1234"));
        when(userRepository.existsByUsername(requestDto.username())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUser(requestDto)).thenReturn(user);

        UserRegistrationResponseDto responseDto = userService.register(requestDto);

        assertEquals("test", responseDto.username());
    }
}
