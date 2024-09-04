package com.globant.project.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.globant.project.domain.entities.Category;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * ProductDTO
 */
@Data
@Builder
public class ProductDTO {

    private UUID uuid;

    @NotEmpty(message = "Fantasy name is required")
    @Column(length = 255, unique = true)
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String fantasyName;

    @NotNull(message = "Category is required")
    private Category category;

    @NotEmpty(message = "Description is required")
    @Size(min = 1, max = 511, message = "Description must be between 1 and 255 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMax(value = "999999.99", message = "Price must be less than 999999.99")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    private Boolean available;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
