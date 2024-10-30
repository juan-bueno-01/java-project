package com.globant.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClientInvoiceDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceClientDTO {

    private String id_number;

    private String name;

}
