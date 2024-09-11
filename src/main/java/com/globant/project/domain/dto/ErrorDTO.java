package com.globant.project.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

    private String errorCode;
    private String errorType;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
