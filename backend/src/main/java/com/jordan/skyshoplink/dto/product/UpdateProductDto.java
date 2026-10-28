package com.jordan.skyshoplink.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductDto(
        String name,
        String descritpion,
        Integer stockQuantity,
        BigDecimal price,
        UUID categoryId
) {
}
