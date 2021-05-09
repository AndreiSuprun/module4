package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Class for validating user password
 */

@Component
public class QuantityValidator implements Validator<Integer> {

    private final static int QUANTITY_MIN_VALUE = 1;
    private final static int QUANTITY_MAX_VALUE = 1000;

    @Override
    public boolean isValid(Integer quantity) {
        return Objects.nonNull(quantity) &&
                quantity >= QUANTITY_MIN_VALUE &&
                quantity <= QUANTITY_MAX_VALUE;
    }
}
