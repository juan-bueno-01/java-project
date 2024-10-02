package com.globant.project.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DeliveryEntity
 */
@Entity
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_document")
    private ClientEntity clientDocument;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_uuid")
    private ProductEntity productUuid;

    @Min(value = 1, message = "Quantity must be greater than 0")
    @Max(value = 100, message = "Quantity must be less than 100")
    private Integer quantity;

    @Size(max = 511, message = "Extra information must be less than 255 characters")
    private String extraInformation;

    @DecimalMax(value = "999999999.99", message = "SubTotal must be less than 999999.99")
    @DecimalMin(value = "0.01", message = "SubTotal must be greater than 0.01")
    @Column(precision = 11, scale = 2)
    private BigDecimal subTotal;

    @DecimalMax(value = "999999999.99", message = "Tax must be less than 999999.99")
    @DecimalMin(value = "0.01", message = "Tax must be greater than 0.01")
    @Column(precision = 11, scale = 2)
    private BigDecimal tax;

    @DecimalMax(value = "999999999.99", message = "GrandTotal must be less than 999999.99")
    @DecimalMin(value = "0.01", message = "GrandTotal must be greater than 0.01")
    @Column(precision = 11, scale = 2)
    private BigDecimal grandTotal;

    @Column(nullable = true)
    private Boolean delivered;

    @Column(nullable = true)
    private LocalDateTime deliveredDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public OrderEntity(UUID uuid, ClientEntity client, ProductEntity product, Integer quantity,
            String extraInformation) {
        this.uuid = uuid;
        this.clientDocument = client;
        this.productUuid = product;
        this.quantity = quantity;
        this.extraInformation = extraInformation;
    }
}
