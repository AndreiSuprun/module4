package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.Validator;

import java.util.Objects;
import java.util.Optional;

/**
 * Class for validating user password
 */
public class DurationValidator implements Validator<Integer> {

    private final static int DURATION_MIN_VALUE = 1;
    private final static int DURATION_MAX_VALUE = 180;

    @Override
    public boolean isValid(Integer duration) {
        return Objects.nonNull(duration) &&
                duration >= DURATION_MIN_VALUE &&
                duration <= DURATION_MAX_VALUE;
    }
}
