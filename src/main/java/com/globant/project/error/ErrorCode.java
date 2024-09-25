package com.globant.project.error;

import org.springframework.stereotype.Component;

import com.globant.project.services.impl.ErrorServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorCode {
    private ErrorServiceImpl errorService;

    public String getErrorDescription(String code, String... args) {
        return errorService.getErrorDescription(code, args);
    }

    public String getErrorException(String code, String exception) {
        return errorService.getErrorException(code, exception);
    }
}
