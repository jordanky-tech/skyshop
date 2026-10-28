package com.jordan.skyshoplink.mapper;

import com.jordan.skyshoplink.dto.category.CategoryResponseDto;
import com.jordan.skyshoplink.dto.category.CategorySummary;
import com.jordan.skyshoplink.dto.category.CreateCategoryDto;
import com.jordan.skyshoplink.dto.category.UpdateCategoryDto;
import com.jordan.skyshoplink.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDto toResponse(Category data){
        return CategoryResponseDto.builder()
                .id(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();
    }

    public CategorySummary toSummary(Category data){
        return CategorySummary.builder()
                .id(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .build();
    }

    public Category toEntity(CreateCategoryDto data){
        return Category.builder()
                .name(data.name())
                .description(data.descritpion())
                .build();
    }



    public Category toEntity(UpdateCategoryDto data){
        return Category.builder()
                .name(data.name())
                .description(data.descritpion())
                .build();
    }
}
