package com.globant.project.error;

import lombok.experimental.UtilityClass;

/**
 * ErrorConstants
 */
@UtilityClass
public class ErrorConstants {

    public final String INTERNAL_SERVER_ERROR = "E5000";
    public final String USER_ALREADY_EXIST = "E1001";
    public final String USER_NOT_FOUND = "E1002";
    public final String PRODUCT_ALREADY_EXIST = "E2001";
    public final String PRODUCT_CATEGORY_DOES_NOT_EXIST = "E2002";
    public final String PRODUCT_NOT_FOUND = "E2003";
    public final String PRODUCT_NO_DIFFERENT_FIELD = "E2004";
    public final String ORDER_NOT_FOUND = "E3001";
    public final String ARGUMENT_VALIDATION = "E4001";
    public final String FORMAT_VALIDATION = "E4002";
    public final String ERROR_ALREADY_EXIST = "E5001";
    public final String ERROR_NOT_FOUND = "E5002";

}
