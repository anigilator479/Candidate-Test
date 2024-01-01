package com.example.test.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new product with valid fields")
    @WithMockUser(username = "user")
    void addProduct_ValidValues_Success() {
        TableRequestDto tableRequestDto = new TableRequestDto(
                "notProducts",
                List.of(Map.of("entryDate", "03-01-2023"))
        );

        String jsonRequest = objectMapper.writeValueAsString(tableRequestDto);

        mockMvc.perform(
                        post("/products/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(scripts = {"classpath:database/product/create-products-table.sql",
            "classpath:database/product/add-product.sql"})
    @DisplayName("Get all from products")
    @WithMockUser(username = "user")
    void getAllFromProducts_ValidUser_Success() {

        MvcResult result = mockMvc.perform(
                        get("/products/all")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<ProductResponseDto> actual = objectMapper.readerForListOf(ProductResponseDto.class)
                .readValue(result.getResponse().getContentAsString());

        int actualItemCode = (int) actual.get(0).products().get("item_code");
        int expectedItemCode = 100;
        int actualId = (int) actual.get(0).products().get("id");
        int expectedId = 1;

        Assertions.assertEquals(expectedItemCode, actualItemCode);
        Assertions.assertEquals(expectedId, actualId);
    }
}
