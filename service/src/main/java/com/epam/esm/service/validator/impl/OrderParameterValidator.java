package com.epam.esm.service.validator.impl;

/**
 * Class for validating order parameter of query
 */
public class OrderParameterValidator extends RegExValidator {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 12;
    private static final String NAME_REGEX = ".*(name|date).*";

    public OrderParameterValidator() {
        super(NAME_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
