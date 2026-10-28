package com.jordan.skyshoplink.service;

import com.jordan.skyshoplink.common.response.PagedData;
import com.jordan.skyshoplink.common.response.Pagination;
import com.jordan.skyshoplink.common.utils.PageableSort;
import com.jordan.skyshoplink.dto.product.CreateProductDto;
import com.jordan.skyshoplink.dto.product.ProductResponseDto;
import com.jordan.skyshoplink.dto.product.UpdateProductDto;
import com.jordan.skyshoplink.entity.Product;
import com.jordan.skyshoplink.exception.UnauthorizedException;
import com.jordan.skyshoplink.mapper.ProductMapper;
import com.jordan.skyshoplink.repository.CategoryRepository;
import com.jordan.skyshoplink.repository.ProductRepository;
import com.jordan.skyshoplink.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductSpecification productSpecification;

    public List<ProductResponseDto> getAll(){
        return this.productRepository.findAll().stream()
                .map(this.productMapper::toResponse)
                .toList();
    }

    public ProductResponseDto getById(UUID id){
        return this.productMapper.toResponse(this.productRepository.getReferenceById(id));
    }

    public ProductResponseDto create(CreateProductDto data){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est authentifié (et non anonyme)
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Vous devez être connecté pour effectuer cette action.");
        }
        Product saved = this.productRepository.save(Product.builder()
                .name(data.getName())
                .description(data.getDescription())
                .price(data.getPrice())
                .stockQuantity(data.getStockQuantity())
                .category(this.categoryRepository.getReferenceById(data.getCategoryId()))
                .build());
        return this.productMapper.toResponse(saved);
    }

    public ProductResponseDto update(UpdateProductDto data, UUID id){
        Product old = this.productRepository.getReferenceById(id);
        old.update(data.name(), data.descritpion(), data.stockQuantity(), data.price());
        return productMapper.toResponse(this.productRepository.save(old));
    }

    public void delete(UUID id){
        Product product = this.productRepository.getReferenceById(id);
        this.productRepository.delete(product);
    }

    //

    public PagedData<ProductResponseDto> getWithPaginationAndSort(
            String name,
            UUID categoryId,
            Integer page,
            Integer size,
            String sortBy,
            String direction
    ){
        Pageable pageable = PageableSort.createPageable(page, size, sortBy, direction);
        Specification<Product> filter = productSpecification.filter(name, categoryId);
        Page<Product> all = this.productRepository.findAll(filter, pageable);

        List<ProductResponseDto> responseDtos = all.stream().map(this.productMapper::toResponse).toList();

        Pagination pagination = Pagination.builder()
                .page(page).size(size)
                .totalElements(all.getTotalElements())
                .totalPages(all.getTotalPages())
                .build();

        return PagedData.<ProductResponseDto>builder()
                .list(responseDtos).pagination(pagination)
                .build();
    }


}
