package com.jordan.skyshoplink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder 
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity 
@Table(name = "categories")
public class Category {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, columnDefinition = "uuid")
    private UUID id ;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

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

    public void update(String name, String description){
        if (name != null && !name.isBlank()) this.name = name;
        if (description != null && !description.isBlank()) this.description = description;
    }

}
