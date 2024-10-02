package com.globant.project.domain.dto;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClientDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    @Pattern(regexp = "[A-Z]+-\\d{6,}")
    @NotEmpty(message = "Document is required")
    @Size(min = 1, max = 20, message = "Document must be between 1 and 20 characters")
    private String document;

    @NotEmpty(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters")
    @Email
    private String email;

    @NotEmpty(message = "Phone is required")
    @Size(min = 1, max = 10, message = "Phone must be between 1 and 10 characters")
    private String phone;

    @NotEmpty(message = "Address is required")
    @Size(min = 1, max = 500, message = "Address must be between 1 and 500 characters")
    private String deliveryAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ClientDTO(String document, String name, String email, String phone, String deliveryAddress) {
        this.document = document;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
    }

}
