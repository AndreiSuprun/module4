package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.Validator;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Class for validating periodical price
 */
public class PriceValidator implements Validator<BigDecimal> {

    private final static BigDecimal MIN_VALUE = new BigDecimal(1.00);
    private final static BigDecimal MAX_VALUE = new BigDecimal(10000L);

    @Override
    public boolean isValid(BigDecimal price) {
        return Objects.nonNull(price) && MIN_VALUE.compareTo(price) <= 0
                && MAX_VALUE.compareTo(price) > 0;
    }
}
