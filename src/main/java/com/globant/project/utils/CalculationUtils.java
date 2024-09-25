package com.globant.project.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.experimental.UtilityClass;

/**
 * CalculationUtils
 */
@UtilityClass
public class CalculationUtils {

    public BigDecimal calculateSubTotal(BigDecimal price, int quantity) {
        return price.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.FLOOR);
    }

    public BigDecimal calculateTax(BigDecimal subTotal) {
        return subTotal.multiply(GlobalUtils.TAX).setScale(2, RoundingMode.FLOOR);
    }

    public BigDecimal calculateGrandTotal(BigDecimal subTotal, BigDecimal tax) {
        return subTotal.add(tax).setScale(2, RoundingMode.FLOOR);
    }
}
