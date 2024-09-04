package com.globant.project.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "productUuid")
    private Set<OrderEntity> orders;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductEntity other = (ProductEntity) obj;
        if (fantasyName == null) {
            if (other.fantasyName != null)
                return false;
        } else if (!fantasyName.equals(other.fantasyName))
            return false;
        if (category != other.category)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (available == null) {
            if (other.available != null)
                return false;
        } else if (!available.equals(other.available))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fantasyName == null) ? 0 : fantasyName.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((available == null) ? 0 : available.hashCode());
        return result;
    }

}
