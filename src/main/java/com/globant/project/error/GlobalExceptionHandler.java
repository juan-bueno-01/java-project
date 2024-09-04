package com.globant.project.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;

/**
 * ExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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
        return buildErrorResponse("E4002 " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleDataValidationException(MethodArgumentNotValidException ex) {
        return buildErrorResponse("E4001 " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleProductCategoryException(HttpMessageNotReadableException ex) {
        return buildErrorResponse("E2002 " + ex.getClass().getName() + ex.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return buildErrorResponse("E5000 " + ex.getClass().getName() + ex.getMessage(),
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
