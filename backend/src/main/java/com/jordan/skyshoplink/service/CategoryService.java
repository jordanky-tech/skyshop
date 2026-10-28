package com.jordan.skyshoplink.service;

import com.jordan.skyshoplink.dto.category.CategoryResponseDto;
import com.jordan.skyshoplink.dto.category.CreateCategoryDto;
import com.jordan.skyshoplink.dto.category.UpdateCategoryDto;
import com.jordan.skyshoplink.entity.Category;
import com.jordan.skyshoplink.exception.UnauthorizedException;
import com.jordan.skyshoplink.mapper.CategoryMapper;
import com.jordan.skyshoplink.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> getAll(){
        List<Category> all = this.categoryRepository.findAll();
        return all.stream()
                .map(this.categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponseDto create(CreateCategoryDto data){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est authentifié (et non anonyme)
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Vous devez être connecté pour effectuer cette action.");
        }
        return this.categoryMapper.toResponse(this.categoryRepository.save(this.categoryMapper.toEntity(data)));
    }

    public CategoryResponseDto update(UpdateCategoryDto data, UUID id){
        Category old = this.categoryRepository.getReferenceById(id);
        old.update(data.name(), data.descritpion());
        return this.categoryMapper.toResponse(this.categoryRepository.save(old));
    }

    public void delete(UUID id){
        this.categoryRepository.deleteById(id);
    }

    public CategoryResponseDto getById(UUID id) {
        return this.categoryMapper.toResponse(this.categoryRepository.getReferenceById(id));
    }
}
