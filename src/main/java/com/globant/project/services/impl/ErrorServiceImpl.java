package com.globant.project.services.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.domain.entities.ErrorEntity;
import com.globant.project.error.ErrorConstants;
import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.mappers.ErrorMapper;
import com.globant.project.repositories.ErrorRepository;
import com.globant.project.services.ErrorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class ErrorServiceImpl implements ErrorService {

    private final ErrorRepository errorRepository;
    private final ErrorMapper errorMapper;

    private Map<String, String> errorCodeMap = new HashMap<>();

    @Transactional
    @Override
    public ErrorDTO createError(ErrorDTO errorDto) {
        String errorCode = errorDto.getErrorCode();
        if (errorExists(errorCode)) {
            throw new ConflictException(getErrorDescription(ErrorConstants.ERROR_ALREADY_EXIST, errorCode));
        }
        ErrorEntity errorEntity = errorMapper.DtoToEntity(errorDto);
        ErrorEntity savedError = errorRepository.save(errorEntity);
        log.info("Error created with code: {}", errorCode);
        refreshErrorCodes();
        return errorMapper.EntityToDto(savedError);
    }

    @Transactional
    @Override
    public void deleteError(String errorCode) {
        ErrorEntity errorEntity = getErrorEntity(errorCode);
        errorRepository.delete(errorEntity);
        refreshErrorCodes();
    }

    @Override
    public ErrorDTO getError(String errorCode) {
        return errorMapper.EntityToDto(getErrorEntity(errorCode));
    }

    @Override
    public void updateError(String errorCode, ErrorDTO errorDto) {
        errorDto.setErrorCode(errorCode);
        if (!errorExists(errorCode)) {
            throw new NotFoundException(getErrorDescription(ErrorConstants.ERROR_NOT_FOUND, errorCode));
        }
        errorRepository.save(errorMapper.DtoToEntity(errorDto));
        log.info("Error updated with code: {}", errorCode);
        refreshErrorCodes();
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean errorExists(String errorCode) {
        return errorRepository.existsById(errorCode);
    }

    private ErrorEntity getErrorEntity(String errorCode) {
        return errorRepository.findById(errorCode)
                .orElseThrow(
                        () -> new NotFoundException(getErrorDescription(ErrorConstants.ERROR_NOT_FOUND, errorCode)));
    }

    @Override
    public List<ErrorDTO> getErrors() {
        return errorRepository.findAll().stream().map(errorMapper::EntityToDto).toList();
    }

    @Cacheable("errorCodes")
    public Map<String, String> loadErrorCodes() {
        List<ErrorEntity> errors = errorRepository.findAll();
        Map<String, String> errorMap = new HashMap<>();
        for (ErrorEntity error : errors) {
            errorMap.put(error.getErrorCode(), error.getErrorType() + ":" + error.getDescription());
        }
        return errorMap;
    }

    @Scheduled(fixedRate = 600000)
    public void refreshErrorCodes() {
        errorCodeMap = loadErrorCodes();
        log.info("Error codes refreshed.");
    }

    @Override
    public String getErrorDescription(String code, String... args) {
        if (!errorCodeMap.containsKey(code)) {
            return "Unknown error";
        }
        String description = errorCodeMap.get(code).split(":")[1];
        if (description == null) {
            return "Unknown error";
        }
        return String.format(description, (Object[]) args).strip();
    }

    @Override
    public String getErrorException(String code, String exception) {
        if (!errorCodeMap.containsKey(code)) {
            return "Unknown exception";
        }
        String errorType = errorCodeMap.get(code).split(":")[0];
        if (errorType == null) {
            return "Unknown exception";
        }
        return errorType + ": " + exception;
    }
}
