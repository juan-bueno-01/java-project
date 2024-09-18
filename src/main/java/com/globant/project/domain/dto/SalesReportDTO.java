package com.globant.project.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SalesReportDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportDTO {
    List<ProductSalesDTO> productSales;

    List<ProductSalesDTO> leastSoldProducts;

    List<ProductSalesDTO> mostSoldProducts;

}
