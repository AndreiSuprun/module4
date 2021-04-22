package com.epam.esm.service.validator.impl;

/**
 * Class for validating name
 */
public class NameValidator extends RegExValidator {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;
    private static final String NAME_REGEX = "[\\p{L}\\p{Digit}\\p{Punct}\\p{Space}]+";

    public NameValidator() {
        super(NAME_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
