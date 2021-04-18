package com.epam.esm.service.validator.impl;

/**
 * Class for validating user first and last name
 */
public class NameValidator extends RegExValidator {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;
    private static final String USER_NAME_REGEX = "^[\\p{Lu}][\\p{L}]+";

    public NameValidator() {
        super(USER_NAME_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
