package com.globant.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceResponse
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {

    private InvoiceResponseData data;

}
