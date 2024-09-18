package com.globant.project.error;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * ErrorCode
 */
@Component
public class ErrorCode {
    private static final Map<String, String> errorCodes = Map.of(
            ErrorConstants.INTERNAL_SERVER_ERROR, "InternalServerErrorException - Internal server error",

            ErrorConstants.USER_ALREADY_EXIST, "UserAlreadyExistException - User with document %s already exists",
            ErrorConstants.USER_NOT_FOUND, "UserNotFoundException - User with document %s not found",

            ErrorConstants.PRODUCT_ALREADY_EXIST,
            "ProductAlreadyExistException - Product with fantasy name %s already exists",
            ErrorConstants.PRODUCT_CATEGORY_DOES_NOT_EXIST,
            "ProductCategorylDoesNotExistException - Product category name is not valid",
            ErrorConstants.PRODUCT_NOT_FOUND, "ProductNotFoundException - Product with uuid %s not found",
            ErrorConstants.PRODUCT_NO_DIFFERENT_FIELD,
            "ProductNoDifferentFieldException -  There is no different field in the Request for product with uuid %s",

            ErrorConstants.ORDER_NOT_FOUND, "OrderNotFoundException - Order with uuid %s not found",

            ErrorConstants.ARGUMENT_VALIDATION, "ArgumentValidationException - Invalid or incomplete data",
            ErrorConstants.FORMAT_VALIDATION, "FormatValidationException - Ivalid format"

    );

    @Cacheable("errorDescriptions")
    public static String getErrorDescription(String code, String... args) {
        if (errorCodes.get(code) == null) {
            return "Unknown error";
        }
        return String.format(errorCodes.get(code).split("-")[1], (Object[]) args).strip();
    }

    @Cacheable("errorExceptions")
    public static String getErrorException(String code, String exception) {
        if (errorCodes.get(code) == null) {
            return "Unknown exception";
        }
        return errorCodes.get(code).split("-")[0] + ": " + exception;
    }
}
