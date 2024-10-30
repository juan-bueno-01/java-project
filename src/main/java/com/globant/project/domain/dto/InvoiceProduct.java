package com.globant.project.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceProduct
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceProduct {
    private Integer quantity;

    private BigDecimal cost;

    private String tax_name1;

    private BigDecimal tax_rate1;

}
