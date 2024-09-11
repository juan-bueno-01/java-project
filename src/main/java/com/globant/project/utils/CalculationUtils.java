package com.globant.project.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CalculationUtils
 */
public class CalculationUtils {

    public static BigDecimal calculateSubTotal(BigDecimal price, int quantity) {
        return price.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.FLOOR);
    }

    public static BigDecimal calculateTax(BigDecimal subTotal, BigDecimal tax) {
        return subTotal.multiply(tax).setScale(2, RoundingMode.FLOOR);
    }

    public static BigDecimal calculateGrandTotal(BigDecimal subTotal, BigDecimal tax) {
        return subTotal.add(tax).setScale(2, RoundingMode.FLOOR);
    }
}
