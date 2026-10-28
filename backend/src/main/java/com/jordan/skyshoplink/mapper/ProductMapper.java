package com.jordan.skyshoplink.mapper;


import com.jordan.skyshoplink.dto.product.ProductResponseDto;
import com.jordan.skyshoplink.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private  final CategoryMapper categoryMapper;
    public ProductResponseDto toResponse(Product data){
        return ProductResponseDto.builder()
                .id(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .price(data.getPrice())
                .stockQuantity(data.getStockQuantity())
                .category(this.categoryMapper.toSummary(data.getCategory()))
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();
    }
}
