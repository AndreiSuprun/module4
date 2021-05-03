package com.epam.esm.service.validator.impl;

public class UserNameValidator extends RegExValidator {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 255;
    private static final String USER_NAME_REGEX = "^[\\p{Lu}][\\p{L}]+";

    public UserNameValidator() {
        super(USER_NAME_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
