package com.jordan.skyshoplink.controller;

import com.jordan.skyshoplink.common.response.ApiResponse;
import com.jordan.skyshoplink.common.response.ResponseBuilder;
import com.jordan.skyshoplink.dto.category.CategoryResponseDto;
import com.jordan.skyshoplink.dto.category.CreateCategoryDto;
import com.jordan.skyshoplink.dto.category.UpdateCategoryDto;
import com.jordan.skyshoplink.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "gestion des categories", description = "gestion des categories")
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    @Operation(description = "recuperer toutes les categories", summary = "recuperer toutes les categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAll() {
        return ResponseBuilder.success(this.categoryService.getAll(), "liste des categories !");
    }

    @GetMapping("/{id}")
    @Operation(description = "get with id", summary = "get with id")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getById(@PathVariable UUID id) {
        return ResponseBuilder.success(this.categoryService.getById(id), "categorie avec l'id : " + id);
    }

    @PostMapping("")
    @Operation(description = "créer une categorie", summary = "créer une categorie")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> create(@RequestBody CreateCategoryDto data) {
        return ResponseBuilder.created(this.categoryService.create(data), "categorie crée avec success !");
    }

    @PutMapping("/{id}")
    @Operation(description = "modifier une categorie", summary = "modifier une categorie")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> update(@RequestBody UpdateCategoryDto data, @PathVariable UUID id) {
        return ResponseBuilder.success(this.categoryService.update(data, id), "categorie modifiée avec success !");
    }

    @DeleteMapping("/{id}")
    @Operation(description = "supprimer une categorie", summary = "supprimer une categorie")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        this.categoryService.delete(id);
        return ResponseBuilder.success(null, "categorie supprimée avec success !");
    }


}
