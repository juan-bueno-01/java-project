package com.globant.project.utils;

/**
 * RegexUtils
 */
public class RegexUtils {

    public static final String DOCUMENT_REGEX = "[A-Z]+-\\d{6,}";
    public static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String DATE_REGEX = "\\d{8}";

}
