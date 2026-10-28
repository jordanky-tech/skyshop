package com.jordan.skyshoplink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true)
    private  String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void initDate(){
        LocalDateTime now =  LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    @PreUpdate
    public void initUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, String description, Integer stockQuantity, BigDecimal price){
        if (name != null && !name.isBlank()) this.name = name;
        if (description != null && !description.isBlank()) this.description = description;
        if (stockQuantity != null) this.stockQuantity = stockQuantity;
        if (price != null) this.price = price;
    }

}
