package com.globant.project.error;

import java.util.Map;

/**
 * ErrorCode
 */
public class ErrorCode {
    private static final Map<String, String> errorCodes = Map.of(
            "E5000", "InternalServerErrorException - Internal server error",

            "E1001", "UserAlreadyExistException - User with document %s already exists",
            "E1002", "UserNotFoundException - User with document %s not found",

            "E2001", "ProductAlreadyExistException - Product with fantasy name %s already exists",
            "E2002", "ProductCategorylDoesNotExistException - Product category name is not valid",
            "E2003", "ProductNotFoundException - Product with uuid %s not found",
            "E2004",
            "ProductNoDifferentFieldException -  There is no different field in the Request for product with uuid %s",

            "E4001", "ArgumentValidationException - Invalid or incomplete data",
            "E4002", "FormatValidationException - Ivalid format"

    );

    public static String getErrorDescription(String code, String... args) {
        if (errorCodes.get(code) == null) {
            return "Unknown error";
        }
        return String.format(errorCodes.get(code).split("-")[1], (Object[]) args).strip();
    }

    public static String getErrorException(String code, String exception) {
        if (errorCodes.get(code) == null) {
            return "Unknown exception";
        }
        return errorCodes.get(code).split("-")[0] + ": " + exception;
    }
}
