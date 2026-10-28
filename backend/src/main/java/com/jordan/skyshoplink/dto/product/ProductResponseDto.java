package com.jordan.skyshoplink.dto.product;

import com.jordan.skyshoplink.dto.category.CategorySummary;
import com.jordan.skyshoplink.entity.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private CategorySummary category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
