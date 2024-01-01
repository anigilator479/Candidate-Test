package com.example.test.service;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import java.util.List;

public interface ProductService {
    void add(TableRequestDto tableRequestDto);

    List<ProductResponseDto> getAll();
}
