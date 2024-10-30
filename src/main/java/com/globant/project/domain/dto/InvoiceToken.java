package com.globant.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceTokenDTO
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceToken {

    private String token;

}
