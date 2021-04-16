package com.epam.esm.service.validator.impl;

/**
 * Class for validating periodical name
 */
public class TitleValidator extends RegExValidator {

    private static final int MAX_LENGTH = 255;
    private static final String TITLE_REGEX = "[\\p{L}\\p{Digit}\\p{Space}]+";

    public TitleValidator() {
        super(TITLE_REGEX, MAX_LENGTH);
    }
}
