package com.globant.project.application.utils;

import lombok.experimental.UtilityClass;

/**
 * RegexUtils
 */
@UtilityClass
public class RegexUtils {

    public final String DOCUMENT_REGEX = "[A-Z]+-\\d{6,}";
    public final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public final String DATE_REGEX = "\\d{8}";
    public final String ERROR_CODE_REGEX = "E\\d{4}";

}
