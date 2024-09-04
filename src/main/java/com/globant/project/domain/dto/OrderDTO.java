package com.globant.project.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * DeliveryDTO
 */
@Data
@Builder
public class OrderDTO {
    private UUID uuid;

    @NotEmpty(message = "Client document is required")
    @Pattern(regexp = "[A-Z]+-\\d{6,}")
    private String clientDocument;

    @NotEmpty
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String productUuid;

    @NotNull(message = "Quantity is required")
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
