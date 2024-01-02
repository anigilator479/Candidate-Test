package com.example.test.service.impl;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import com.example.test.mapper.product.DynamicObjectRowMapper;
import com.example.test.service.ProductService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final int FIRST_RECORD = 0;
    private final JdbcTemplate jdbcTemplate;
    private final ProductUtil productUtil;

    @Transactional
    @Override
    public void add(TableRequestDto tableRequestDto) {
        String tableName = productUtil.validateAndSanitizeTableName(tableRequestDto.table());

        List<Map<String, String>> records = tableRequestDto.records();

        createTableIfNotExist(records.get(FIRST_RECORD), tableName);

        tableRequestDto.records().forEach(product -> saveRecord(tableName, product));
    }

    @Override
    public List<ProductResponseDto> getAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new DynamicObjectRowMapper()).stream()
                 .map(ProductResponseDto::new)
                 .toList();
    }

    private void createTableIfNotExist(Map<String, String> productMap, String tableName) {
        StringBuilder fieldNames = productUtil.createQueryKeys(productMap, " VARCHAR(255), ");

        String createTableQuery = String.format(
                "CREATE TABLE IF NOT EXISTS %s (id BIGINT AUTO_INCREMENT PRIMARY KEY, %s)",
                tableName, fieldNames);

        jdbcTemplate.execute(createTableQuery);
    }

    private void saveRecord(String tableName, Map<String, String> product) {
        StringBuilder keys = productUtil.createQueryKeys(product, ",");

        String values = productUtil.createQueryValues(product);

        jdbcTemplate.update(String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, keys, values));
    }
}
