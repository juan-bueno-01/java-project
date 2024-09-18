package com.globant.project.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductSalesReportDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesDTO {
    private String fantasyName;

    private Long totalSales;

    private BigDecimal totalIncome;

}
