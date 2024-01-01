package com.example.test.dto.product;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.constraints.Length;

public record TableRequestDto(
        @Length(min = 4, max = 30)
        String table,
        @NotEmpty
        List<Map<String, String>> records
) {
}
