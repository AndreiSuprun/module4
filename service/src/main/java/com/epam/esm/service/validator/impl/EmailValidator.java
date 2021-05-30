package com.epam.esm.service.validator.impl;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator extends RegExValidator {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 255;
    private static final String EMAIL_REGEX = "^(.+@.+\\..+)$";

    public EmailValidator() {
        super(EMAIL_REGEX, MIN_LENGTH, MAX_LENGTH);
    }
}
