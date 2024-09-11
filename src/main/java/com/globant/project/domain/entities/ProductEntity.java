package com.globant.project.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductEntity
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(length = 255, unique = true)
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String fantasyName;

    private Category category;

    @Size(min = 1, max = 511, message = "Description must be between 1 and 255 characters")
    private String description;

    @DecimalMax(value = "999999.99", message = "Price must be less than 999999.99")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    private Boolean available;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "productUuid")
    // private Set<OrderEntity> orders;

}
