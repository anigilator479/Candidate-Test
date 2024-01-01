package com.example.test.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserAuthenticationRequestDto(
        @Length(min = 4, max = 50)
        @NotBlank
        String username,
        @Length(min = 4, max = 50)
        @NotBlank
        String password
) {
}
