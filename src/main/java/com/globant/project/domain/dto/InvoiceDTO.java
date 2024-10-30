package com.globant.project.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private String client_id;

    private List<InvoiceProduct> line_items;

    private BigDecimal partial;

}
