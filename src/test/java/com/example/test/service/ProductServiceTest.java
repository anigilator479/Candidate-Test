package com.example.test.service;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import com.example.test.mapper.product.DynamicObjectRowMapper;
import com.example.test.service.impl.ProductServiceImpl;
import com.example.test.service.impl.ProductUtil;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Spy
    private ProductUtil productUtil;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("add product")
    public void addProduct_ValidRequestDto_Success() {
        TableRequestDto tableRequestDto = new TableRequestDto(
                "products",
                List.of(Map.of("item_code", "100"))
        );

        Mockito.doNothing().when(jdbcTemplate).execute(Mockito.anyString());
        Mockito.when(jdbcTemplate.update(Mockito.anyString())).thenReturn(1);

        productService.add(tableRequestDto);

        Mockito.verify(jdbcTemplate).execute(Mockito.anyString());
        Mockito.verify(jdbcTemplate).update(Mockito.anyString());
    }

    @Test
    @DisplayName("Get all from products")
    public void getAllProducts_Valid_Success() {

        List<ProductResponseDto> expected = List.of(new ProductResponseDto(Map.of(
                "id", "1",
                "item_code", "100"))
        );

        Mockito.when(jdbcTemplate.query(Mockito.anyString(),
                        Mockito.any(DynamicObjectRowMapper.class)))
                .thenReturn(List.of(Map.of(
                        "id", "1",
                        "item_code", "100")));

        List<ProductResponseDto> actual = productService.getAll();

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(),
                Mockito.any(DynamicObjectRowMapper.class));

        Assertions.assertEquals(expected, actual);
    }
}
