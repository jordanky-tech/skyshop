package com.jordan.skyshoplink.specification;

import com.jordan.skyshoplink.entity.Category;
import com.jordan.skyshoplink.entity.Product;
import com.jordan.skyshoplink.repository.CategoryRepository;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductSpecification {
    private  final CategoryRepository categoryRepository;
    public Specification<Product> byName(String name){
        return (root, query, cb) -> {
            if (name == null || name.trim().isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
    public Specification<Product> byCategoryId(UUID categoryId){
        return (root, query, cb) -> {
            if (categoryId == null) return cb.conjunction();
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public Specification<Product> byCategoryIdJoin(UUID categoryId){
        return (root, query, cb) -> {
            if (categoryId == null) return cb.conjunction();
            Join<Product, Category> categoryJoin = (Join<Product, Category>) root.<Product, Category>fetch("category");
            return cb.equal(categoryJoin.get("id"), categoryId);
        };
    }

    public Specification<Product> filter(String name, UUID categoryId){
        return Specification.where(this.byName(name))
                .and(this.byCategoryId(categoryId));
    }
}
