package com.epam.esm.service.validator.impl;

/**
 * Class for validating periodical description
 */
public class DescriptionValidator extends RegExValidator {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 500;
    private static final String DESCRIPTION_REGEX = "[\\p{L}\\p{Digit}\\p{Punct}\\p{Space}]+";

    public DescriptionValidator() {
        super(DESCRIPTION_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
