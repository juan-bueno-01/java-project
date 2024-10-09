package com.globant.project.application.ports.in.services;

import java.util.List;

import com.globant.project.domain.dto.ErrorDTO;

/**
 * ErrorService
 */
public interface ErrorService {

    ErrorDTO createError(ErrorDTO errorDto);

    void deleteError(String errorCode);

    ErrorDTO getError(String errorCode);

    List<ErrorDTO> getErrors();

    void updateError(String errorCode, ErrorDTO errorDto);

    Boolean errorExists(String uuid);

    String getErrorDescription(String code, String... args);

    String getErrorException(String code, String exception);

}
