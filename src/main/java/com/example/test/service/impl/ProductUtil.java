package com.example.test.service.impl;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductUtil {
    private static final String COMA = ",";
    private static final String APOSTROPHE = "'";

    public String validateAndSanitizeTableName(String tableName) {
        if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(String.format("Invalid table name: %s", tableName));
        }
        return tableName;
    }

    public StringBuilder createQueryKeys(Map<String, String> productMap, String str) {
        StringBuilder fieldNames = new StringBuilder();
        for (String s : productMap.keySet()) {
            fieldNames.append(s).append(str);
        }
        fieldNames.deleteCharAt(fieldNames.lastIndexOf(COMA));
        return fieldNames;
    }

    public String createQueryValues(Map<String, String> product) {
        StringBuilder values = new StringBuilder();

        String joinedValues = product.values().stream()
                .map(s -> APOSTROPHE + s + APOSTROPHE)
                .collect(Collectors.joining(COMA));

        values.append(joinedValues);

        return values.toString();
    }
}
