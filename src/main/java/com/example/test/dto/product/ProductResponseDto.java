package com.example.test.dto.product;

import java.util.Map;

public record ProductResponseDto(
        Map<String, Object> products
) {
}
