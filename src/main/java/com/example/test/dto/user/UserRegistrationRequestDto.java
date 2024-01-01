package com.example.test.dto.user;

import org.hibernate.validator.constraints.Length;

public record UserRegistrationRequestDto(
        @Length(min = 4, max = 30)
        String username,
        @Length(min = 4, max = 100)
        String password
) {
}
