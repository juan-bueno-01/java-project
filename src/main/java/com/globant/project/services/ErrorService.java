package com.globant.project.services;

import com.globant.project.domain.dto.ErrorDTO;

/**
 * ErrorService
 */
public interface ErrorService {

    void createError(ErrorDTO errorDto);

    void deleteError(String uuid);

    ErrorDTO getError(String uuid);

    void updateError(String uuid, ErrorDTO errorDto);

    Boolean errorExists(String uuid);

}
