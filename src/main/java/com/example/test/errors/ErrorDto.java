package com.example.test.errors;

import java.time.LocalDateTime;

public record ErrorDto(
        LocalDateTime timestamp,
        Object errorPayload) {
}
