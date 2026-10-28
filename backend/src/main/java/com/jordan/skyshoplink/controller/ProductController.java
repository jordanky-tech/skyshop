package com.jordan.skyshoplink.controller;


import com.jordan.skyshoplink.common.response.ApiResponse;
import com.jordan.skyshoplink.common.response.PagedData;
import com.jordan.skyshoplink.common.response.ResponseBuilder;
import com.jordan.skyshoplink.dto.product.CreateProductDto;
import com.jordan.skyshoplink.dto.product.ProductResponseDto;
import com.jordan.skyshoplink.dto.product.UpdateProductDto;
import com.jordan.skyshoplink.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "gestion des produits", description = "gestion des produits")
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    @Operation(description = "recuperer tous les produits", summary = "recuperer tous les produits")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAll(){
        return ResponseBuilder.success(this.productService.getAll(), "liste des produits !");
    }
    @GetMapping("/{id}")
    @Operation(description = "get with id", summary = "get with id")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getById(@PathVariable UUID id){
        return ResponseBuilder.success(this.productService.getById(id), "produit avec l'id : " + id);
    }

    @GetMapping("paginated")
    @Operation(description = "recuperer une liste paginée de produits", summary = "recuperer une liste paginée de produits")
    public ResponseEntity<ApiResponse<PagedData<ProductResponseDto>>> getPaginated(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID catgoryId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "name") String sortby
            ){
        PagedData<ProductResponseDto> withPaginationAndSort = this.productService
                .getWithPaginationAndSort(name, catgoryId, page, size, sortby, direction);

        return ResponseBuilder.success(withPaginationAndSort, "liste de produit paginée");
    }

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "créer un produit", summary = "créer un produit")
    public ResponseEntity<ApiResponse<ProductResponseDto>> create(@ModelAttribute CreateProductDto data){
        return ResponseBuilder.created(this.productService.create(data), "produit crée avec success !");
    }

    @PutMapping("/{id}")
    @Operation(description = "modifier un produit", summary = "modifier un produit")
    public ResponseEntity<ApiResponse<ProductResponseDto>> update(@RequestBody UpdateProductDto data, @PathVariable UUID id){
        return ResponseBuilder.success(this.productService.update(data, id), "produit modifiée avec success !");
    }

    @DeleteMapping("/{id}")
    @Operation(description = "supprimer un produit", summary = "supprimer un produit")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id){
        this.productService.delete(id);
        return ResponseBuilder.success(null, "produit supprimée avec success !");
    }
}
