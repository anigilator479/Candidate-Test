package com.example.test.controller;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import com.example.test.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product management", description = "Endpoints for managing products")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add new product", description = "users can add new products")
    @PostMapping("/add")
    public void add(@RequestBody @Valid TableRequestDto request) {
        productService.add(request);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Register", description = "Users can create new account")
    @GetMapping("/all")
    public List<ProductResponseDto> getAll() {
        return productService.getAll();
    }
}
