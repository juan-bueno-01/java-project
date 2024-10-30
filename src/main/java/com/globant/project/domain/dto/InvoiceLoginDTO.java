package com.globant.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceLoginDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceLoginDTO {

    private String email;

    private String password;

}
