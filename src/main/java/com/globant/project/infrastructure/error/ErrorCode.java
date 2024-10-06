package com.globant.project.infrastructure.error;

import org.springframework.stereotype.Component;

import com.globant.project.application.ports.in.services.ErrorService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorCode {
    private final ErrorService errorService;

    public String getErrorDescription(String code, String... args) {
        return errorService.getErrorDescription(code, args);
    }

    public String getErrorException(String code, String exception) {
        return errorService.getErrorException(code, exception);
    }
}
