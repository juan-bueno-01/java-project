package com.globant.project.infrastructure.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.globant.project.application.utils.ErrorConstants;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionHandler
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @Autowired
    private final ErrorCode ErrorCode;

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFormatValidationException(HandlerMethodValidationException ex) {
        return buildErrorResponse(ErrorConstants.FORMAT_VALIDATION + " " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleDataValidationException(MethodArgumentNotValidException ex) {
        return buildErrorResponse(ErrorConstants.FORMAT_VALIDATION + " " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleProductCategoryException(HttpMessageNotReadableException ex) {
        return buildErrorResponse(
                ErrorConstants.PRODUCT_CATEGORY_DOES_NOT_EXIST + " " + ex.getClass().getName() + ex.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return buildErrorResponse(
                ErrorConstants.INTERNAL_SERVER_ERROR + " " + ex.getClass().getName() + ExceptionUtils.getStackTrace(ex),
                HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        String[] errorArgs = message.substring(6).split(" ");
        String errorCode = message.split(" ")[0];
        String errorMessage = ErrorCode.getErrorDescription(errorCode, errorArgs);
        String errorException = ErrorCode.getErrorException(errorCode, message.substring(6));

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", errorCode);
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("description", errorMessage);
        errorAttributes.put("exception", errorException);

        return new ResponseEntity<>(errorAttributes, status);
    }

}
