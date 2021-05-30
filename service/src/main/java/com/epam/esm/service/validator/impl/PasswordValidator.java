package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PasswordValidator implements Validator<String> {

    private final static int PASSWORD_MIN_LENGTH = 5;
    private final static int PASSWORD_MAX_LENGTH = 255;

    @Override
    public boolean isValid(String password) {
        return Objects.nonNull(password) &&
                password.length() >= PASSWORD_MIN_LENGTH &&
                password.length() <= PASSWORD_MAX_LENGTH;
    }
}