package com.example.test.service.impl;

import com.example.test.dto.product.ProductResponseDto;
import com.example.test.dto.product.TableRequestDto;
import com.example.test.service.ProductService;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final int FIRST = 0;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(TableRequestDto tableRequestDto) {
        String tableName = validateAndSanitizeTableName(tableRequestDto.table());

        List<Map<String, String>> records = tableRequestDto.records();

        createTableIfNotExist(records.get(FIRST), tableName);

        tableRequestDto.records().forEach(product -> saveRecord(tableName, product));
    }

    @Override
    public List<ProductResponseDto> getAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new DynamicObjectRowMapper()).stream()
                 .map(ProductResponseDto::new)
                 .toList();
    }

    private String validateAndSanitizeTableName(String tableName) {
        if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(String.format("Invalid table name: %s", tableName));
        }
        return tableName;
    }

    private void createTableIfNotExist(Map<String, String> productMap, String tableName) {
        StringBuilder fieldNames = createQueryKeys(productMap, " VARCHAR(255), ");

        String createTableQuery = String.format(
                "CREATE TABLE IF NOT EXISTS %s (id BIGINT AUTO_INCREMENT PRIMARY KEY, %s)",
                tableName, fieldNames);

        jdbcTemplate.execute(createTableQuery);
    }

    private static StringBuilder createQueryKeys(Map<String, String> productMap, String str) {
        StringBuilder fieldNames = new StringBuilder();
        for (String s : productMap.keySet()) {
            fieldNames.append(s).append(str);
        }
        fieldNames.deleteCharAt(fieldNames.lastIndexOf(","));
        return fieldNames;
    }

    private void saveRecord(String tableName, Map<String, String> product) {
        StringBuilder keys = createQueryKeys(product, ",");

        String values = createQueryValues(product);

        jdbcTemplate.update(String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, keys, values));
    }

    private String createQueryValues(Map<String, String> product) {
        StringBuilder values = new StringBuilder();

        String joinedValues = product.values().stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));

        values.append(joinedValues);

        return values.toString();
    }

    public static class DynamicObjectRowMapper implements RowMapper<Map<String, Object>> {

        @Override
        public Map<String, Object> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Object> dynamicObject = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                dynamicObject.put(columnName, value);
            }

            return dynamicObject;
        }
    }
}
